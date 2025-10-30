package com.svistoyanov.mj.api.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.svistoyanov.mj.api.dto.AbstractEntityDto;

import java.util.UUID;

public class UserDescriptionDto extends AbstractEntityDto {
    @JsonProperty("email")
    private String email;

    @JsonProperty("username")
    private String username;

    public UserDescriptionDto() {
    }

    public UserDescriptionDto(String email, String username) {
        this.email = email;
        this.username = username;
    }

    public UserDescriptionDto(UUID id, String email, String username) {
        super(id);
        this.email = email;
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
