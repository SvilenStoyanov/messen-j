package com.svistoyanov.mj.api.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.svistoyanov.mj.api.dto.AbstractDto;

import java.util.ArrayList;
import java.util.List;

public class UsersWrapperDto extends AbstractDto {
    @JsonProperty("users")
    private List<UserDescriptionDto> users = new ArrayList<>();

    public UsersWrapperDto() {
    }

    public UsersWrapperDto(List<UserDescriptionDto> users) {
        this.users = users;
    }

    public List<UserDescriptionDto> getUsers() {
        return new ArrayList<>(users);
    }

    public void setUsers(List<UserDescriptionDto> users) {
        this.users = users;
    }
}
