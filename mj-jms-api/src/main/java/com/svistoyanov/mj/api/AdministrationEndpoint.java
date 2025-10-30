package com.svistoyanov.mj.api;

import com.svistoyanov.mj.api.dto.user.SignInDto;
import com.svistoyanov.mj.api.dto.user.SignUpDto;
import com.svistoyanov.mj.api.dto.user.UserDto;
import com.svistoyanov.mj.api.dto.user.UsersFilterDto;
import com.svistoyanov.mj.api.dto.user.UsersWrapperDto;

import java.util.concurrent.CompletableFuture;

public interface AdministrationEndpoint extends Endpoint {

    CompletableFuture<Void> signUp(SignUpDto signUpDto);

    CompletableFuture<UserDto> signIn(SignInDto signInDto);

    CompletableFuture<UsersWrapperDto> loadUsers(UsersFilterDto filter);
}
