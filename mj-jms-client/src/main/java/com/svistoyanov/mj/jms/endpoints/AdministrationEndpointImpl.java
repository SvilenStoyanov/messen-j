package com.svistoyanov.mj.jms.endpoints;

import com.svistoyanov.mj.api.AdministrationEndpoint;
import com.svistoyanov.mj.api.constants.OperationsNames;
import com.svistoyanov.mj.api.dto.user.SignInDto;
import com.svistoyanov.mj.api.dto.user.SignUpDto;
import com.svistoyanov.mj.api.dto.user.UserDto;
import com.svistoyanov.mj.api.dto.user.UsersFilterDto;
import com.svistoyanov.mj.api.dto.user.UsersWrapperDto;
import com.svistoyanov.mj.jms.OperationInvoker;

import java.util.concurrent.CompletableFuture;

public class AdministrationEndpointImpl extends AbstractEndpointImpl implements AdministrationEndpoint {

    public AdministrationEndpointImpl(OperationInvoker operationInvoker) {
        super(operationInvoker);
    }

    @Override
    public CompletableFuture<Void> signUp(SignUpDto signUpDto) {
        return operationInvoker.invoke(signUpDto, OperationsNames.SIGNUP);
    }

    @Override
    public CompletableFuture<UserDto> signIn(SignInDto signInDto) {
        return operationInvoker.invoke(signInDto, OperationsNames.SIGNIN);
    }

    @Override
    public CompletableFuture<UsersWrapperDto> loadUsers(UsersFilterDto filter) {
        return operationInvoker.invoke(filter, OperationsNames.LOAD_USERS);
    }

    @Override
    public void close() {

    }
}
