package com.svistoyanov.mj.mapper;

import com.svistoyanov.mj.api.dto.user.SignInDto;
import com.svistoyanov.mj.api.dto.user.SignUpDto;
import com.svistoyanov.mj.api.dto.user.UserDescriptionDto;
import com.svistoyanov.mj.api.dto.user.UserDto;
import com.svistoyanov.mj.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserMapper {

    public static final UserMapper instance = new UserMapper();

    private UserMapper() {
        //Singleton
    }

    public User signUpDtoToUser(SignUpDto signUpDto) {
        User user = new User(UUID.randomUUID());
        user.setEmail(signUpDto.getEmail());
        user.setUsername(signUpDto.getUsername());
        user.setPassword(signUpDto.getPassword());
        return user;
    }

    public UserDto signInDtoToUserDto(SignInDto signInDto) {
        UserDto userDto = new UserDto();
        userDto.setEmail(signInDto.getEmail());
        userDto.setPassword(signInDto.getPassword());
        userDto.setMessagesCount(0);
        return userDto;
    }

    public List<UserDescriptionDto> usersToUserDtoList(List<User> users) {
        List<UserDescriptionDto> userDtoList = new ArrayList<>();

        for (User user : users) {
            userDtoList.add(userToUserDto(user));
        }
        return userDtoList;
    }

    public UserDescriptionDto userToUserDto(User user) {
        UserDescriptionDto userDto = new UserDescriptionDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        return userDto;
    }
}
