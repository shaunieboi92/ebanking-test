package com.ebanking.uaa.filter;

import java.io.IOException;
import java.text.ParseException;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import org.springframework.util.ObjectUtils;

import org.springframework.web.filter.OncePerRequestFilter;

import com.ebanking.uaa.utility.JwtUtility;

import io.jsonwebtoken.ExpiredJwtException;

public class AuthTokenFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUtility jwtUtility;

	@Autowired
	private UserDetailsService userDetailsService;

	private static final Logger logger = LoggerFactory
			.getLogger(AuthTokenFilter.class);

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		String path = request.getServletPath();
		return path.startsWith("/auth/");
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		try {
//			if (!shouldNotFilter(request)) {
				logger.info("going through filter");
				logger.info("header is now:" + request.getHeader("Authorization"));
				String jwt = jwtUtility.parseJwt(request);
				
				if (!ObjectUtils.isEmpty(jwt) && !ObjectUtils
						.isEmpty(jwtUtility.validateJwtToken(jwt))) {
					String username = jwtUtility.getUserNameFromJwtToken(jwt);

					logger.info("hello" + username);
					UserDetails userDetails = userDetailsService
							.loadUserByUsername(username);
					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					authentication
							.setDetails(new WebAuthenticationDetailsSource()
									.buildDetails(request));

					SecurityContextHolder.getContext()
							.setAuthentication(authentication);
					logger.info("context:"
							+ SecurityContextHolder.getContext().toString());
					logger.info("authorities:" + userDetails.getAuthorities()
							.stream().map(GrantedAuthority::getAuthority)
							.collect(Collectors.toList()));
				}
//			}

		} catch (ExpiredJwtException e) {
			String isRefreshToken = request.getHeader("isRefreshToken");
			String requestURL = request.getRequestURL().toString();

			if (isRefreshToken != null && isRefreshToken.equals("true")
					&& requestURL.contains("refreshtoken")) {
				allowForRefreshToken(e, request);
			} else
				request.setAttribute("exception", e);
//			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);

		} 
		catch (Exception e) {
			logger.error("Cannot set user authentication: {}", e);
		}
//		catch (ParseException
//				| AuthenticationException authenticationException) {
//			response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
//					authenticationException.getMessage());

//		}

//		finally {
			filterChain.doFilter(request, response);
//		}
	}

	private void allowForRefreshToken(ExpiredJwtException ex,
			HttpServletRequest request) {

		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
				null, null, null);

		SecurityContextHolder.getContext()
				.setAuthentication(usernamePasswordAuthenticationToken);
		request.setAttribute("claims", ex.getClaims());

	}

}
