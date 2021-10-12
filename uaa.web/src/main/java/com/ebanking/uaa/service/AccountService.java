package com.ebanking.uaa.service;

import java.util.List;

import javax.validation.Valid;

import com.ebanking.uaa.payload.AccountCreationResponse;
import com.ebanking.uaa.payload.AccountResponse;
import com.ebanking.uaa.payload.CreateUserRequest;

public interface AccountService {

	AccountCreationResponse createUser(@Valid CreateUserRequest request);

	List<AccountResponse> getUserList();

}
