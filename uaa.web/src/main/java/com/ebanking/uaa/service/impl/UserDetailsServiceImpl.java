package com.ebanking.uaa.service.impl;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.ebanking.common.model.User;
import com.ebanking.common.repository.UserRepository;
import com.ebanking.common.util.Constants;
import com.ebanking.uaa.payload.UserDetailsResponse;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	

	private static Logger logger = LoggerFactory
			.getLogger(UserDetailsServiceImpl.class);

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String loginId)
			throws UsernameNotFoundException {
		logger.info("validating user: " + loginId);
		User user = Optional
				.ofNullable(userRepository.findByLoginIdAndDeleted(loginId,
						Constants.FALSE))
				.orElseThrow(() -> new UsernameNotFoundException(loginId));

		List<GrantedAuthority> authorities = buildUserAuthority(user);
		return new UserDetailsResponse(
				user.getLoginId(), user.getPassword(), true, true, true, true,authorities, user.getUserFullName(), user.getUuid());

	}

	private List<GrantedAuthority> buildUserAuthority(User user) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		Set<String> permissionSet = new HashSet<>();
		user.getPermission().forEach(p -> {
			permissionSet.add(p.getPermission());
			logger.info("permissions: " + p.getPermission());
		});

		permissionSet.forEach(permission -> {
			SimpleGrantedAuthority authority = new SimpleGrantedAuthority(permission);
			logger.info("authority:" + authority.toString());
			authorities.add(authority);
		});
		return authorities;
	}

}