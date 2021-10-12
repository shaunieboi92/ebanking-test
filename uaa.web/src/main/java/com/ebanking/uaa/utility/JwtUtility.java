package com.ebanking.uaa.utility;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.ebanking.uaa.payload.JwtResponse;
import com.ebanking.uaa.payload.UserDetailsResponse;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtUtility {

	private static Logger logger = LoggerFactory.getLogger(JwtUtility.class);

	@Value("${jwt.sharedSecret}")
	private String sharedSecret;

	@Value("${jwt.issuer}")
	private String issuer;

	@Value("${jwt.typ}")
	private String typ;

	@Value("${default.password.expiration.min}")
	private int defaultExpMin;

	@Value("${refresh.expiration.min}")
	private int refreshExpMin;
	
	@Resource
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	public JwtResponse generateJwtToken(Authentication authentication) {
		String serializedToken = null;
		List<String> permissions = new ArrayList<>();
		UserDetailsResponse user = (UserDetailsResponse) authentication
				.getPrincipal();
		// set JWTs claims
		Map<String, Object> claims = new HashMap<>();
		claims.put("uuid", user.getUuid());
		claims.put("fullName", user.getFullName());
		claims.put("Roles", permissions);

		Iterator<GrantedAuthority> grantedAuthorities = user.getAuthorities()
				.iterator();
		while (grantedAuthorities.hasNext()) {
			GrantedAuthority ga = grantedAuthorities.next();
			permissions.add(ga.getAuthority());
		}

		serializedToken = Jwts.builder().setSubject((user.getUsername()))
				.claim("data", claims).setIssuedAt(new Date())
				.setExpiration(new Date(
						(new Date()).getTime() + defaultExpMin * 60 * 1000))
				.setAudience(issuer).setHeaderParam("typ", typ)
				.signWith(SignatureAlgorithm.HS512, sharedSecret).compact();

		return new JwtResponse(serializedToken);
	}

	public String getUserNameFromJwtToken(String token) {
		return Jwts.parser().setSigningKey(sharedSecret).parseClaimsJws(token)
				.getBody().getSubject();
	}
	
	public String getUUIDFromJwtToken(String token) {
		 @SuppressWarnings("unchecked")
		Map<String, Object>data = (Map<String, Object>) Jwts.parser().setSigningKey(sharedSecret)
				.parseClaimsJws(token).getBody().get("data");
		 
		 return (String)data.get("uuid");
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getDataFromJwtToken(String token) {
		return (Map<String, Object>) Jwts.parser().setSigningKey(sharedSecret)
				.parseClaimsJws(token).getBody().get("data");
	}

	public String validateJwtToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(sharedSecret).parseClaimsJws(authToken);
			// return true;
			return authToken;
		} catch (SignatureException e) {
			logger.error("Invalid JWT signature: {}", e.getMessage());
			return null;

		} catch (MalformedJwtException e) {
			logger.error("Invalid JWT token: {}", e.getMessage());
			return null;
		} catch (ExpiredJwtException e) {
			logger.error("JWT token is expired: {}", e.getMessage());
			return null;

		} catch (UnsupportedJwtException e) {
			logger.error("JWT token is unsupported: {}", e.getMessage());
			return null;

		} catch (IllegalArgumentException e) {
			logger.error("JWT claims string is empty: {}", e.getMessage());
			return null;

		}

	}

	public String parseJwt(HttpServletRequest request) throws ParseException {
		String headerAuth = request.getHeader("Authorization");
		if (headerAuth == null || headerAuth.isEmpty()
				|| !headerAuth.startsWith("Bearer ")) {
			throw new ParseException("Authorization header parse exception", 0);
		}
		if (StringUtils.hasText(headerAuth)
				&& headerAuth.startsWith("Bearer ")) {
			return headerAuth.substring(7, headerAuth.length());

		}
		return null;
	}
	
	public String returnEncodedPwd(String pwd) {
		return bCryptPasswordEncoder.encode(pwd);
	}
}
