package com.ebanking.uaa.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ebanking.common.exception.BadRequestException;
import com.ebanking.common.model.Permissions;
import com.ebanking.common.model.User;
import com.ebanking.common.model.UserIBANMapping;
import com.ebanking.common.repository.PermissionRepository;
import com.ebanking.common.repository.UserIBANMappingRepository;
import com.ebanking.common.repository.UserRepository;
import com.ebanking.common.util.Constants;
import com.ebanking.uaa.payload.AccountCreationResponse;
import com.ebanking.uaa.payload.AccountResponse;
import com.ebanking.uaa.payload.CreateUserRequest;
import com.ebanking.uaa.service.AccountService;
import com.ebanking.uaa.utility.JwtUtility;

import org.springframework.transaction.annotation.Propagation;

@Service
@Transactional(rollbackFor = Exception.class, propagation = Propagation.NEVER)
public class AccountServiceImpl implements AccountService {

	@Autowired
	JwtUtility jwtUtility;

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserIBANMappingRepository userIBANMappingRepository;

	@Autowired
	PermissionRepository permissionRepository;

	@Override
	public AccountCreationResponse createUser(
			@Valid CreateUserRequest request) {
		User user = new User();
		user.setCreatedBy(Constants.SYSTEM);
		user.setLoginId(request.getUsername());
		user.setPassword(generateHashedPassword(request.getPwd()));
		user.setUserFullName(
				request.getFirstName() + " " + request.getLastName());
		String uuid = UUID.randomUUID().toString();
		user.setUuid(uuid);
		Permissions permission = Optional
				.ofNullable(permissionRepository.findByPermission("CLIENT"))
				.orElseThrow(() -> new BadRequestException("CLIENT"));
		Set<Permissions> permissionSet = new HashSet<>();
		permissionSet.add(permission);
		user.setPermission(permissionSet);
		User savedUser = userRepository.save(user);

		Set<User> users = new HashSet<>();
		users.add(savedUser);

		List<UserIBANMapping> userIbanMapping;
		userIbanMapping = request.getIbanList().stream().map(x -> {
			return new UserIBANMapping(x, savedUser,"SYSTEM");
		}).collect(Collectors.toList());
		userIBANMappingRepository.saveAll(userIbanMapping);
		AccountCreationResponse resp = new AccountCreationResponse();
		resp.setStatus("OK");
		resp.setUsername(request.getUsername());

		return resp;
	}

	private String generateHashedPassword(String pwd) {
		return jwtUtility.returnEncodedPwd(pwd);
	}

	@Override
	public List<AccountResponse> getUserList() {
		return userRepository.findAll().stream().map(x -> {
			return mapUserResponse(x);
		}).collect(Collectors.toList());
	}

	private AccountResponse mapUserResponse(User user) {
		AccountResponse resp = new AccountResponse();
		resp.setFullName(user.getUserFullName());
		resp.setUsername(user.getLoginId());
		resp.setUuid(user.getUuid());
		resp.setDeleted(user.getDeleted());
		Set<String> permissions = user.getPermission().stream().map(x -> {
			return x.getPermission();
		}).collect(Collectors.toSet());
		resp.setPermissions(permissions);
		Set<String> ibanList = user.getIbanList().stream().map(x->{
			return x.getIBAN();
		}).collect(Collectors.toSet());
		resp.setAccountIban(ibanList);
		
		return resp;
	}

}
