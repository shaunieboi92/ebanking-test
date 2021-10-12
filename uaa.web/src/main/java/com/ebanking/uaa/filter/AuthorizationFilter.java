package com.ebanking.uaa.filter;

import java.text.ParseException;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.ebanking.common.util.Constants;
import com.ebanking.uaa.utility.JwtUtility;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

@Component
public class AuthorizationFilter extends ZuulFilter {

	private static Logger logger = LoggerFactory
			.getLogger(AuthorizationFilter.class);
	@Autowired
	private JwtUtility jwtUtil;

	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public int filterOrder() {
		return 0;
	}

	@Override
	public boolean shouldFilter() {
		return true;
	}
	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		String header = "";
		try {
			header = jwtUtil.parseJwt(request);
		} catch (ParseException e) {
			ctx.setResponseStatusCode(401);
			ctx.setSendZuulResponse(false);
		}
		logger.info("context is outside :"
				+ SecurityContextHolder.getContext().toString());

		logger.info("Token is '" + header + "'");
		String responseToken = jwtUtil.validateJwtToken(header);

		ctx.addZuulRequestHeader(Constants.UUID,
				jwtUtil.getUUIDFromJwtToken(responseToken));

		for (Entry<String, String> entry : ctx.getZuulRequestHeaders()
				.entrySet()) {
			logger.info(
					"key: " + entry.getKey() + "| value: " + entry.getValue());

		}

		logger.info(String.format("%s request to %s", request.getMethod(),
				request.getRequestURL().toString()));
		return null;
	}
}