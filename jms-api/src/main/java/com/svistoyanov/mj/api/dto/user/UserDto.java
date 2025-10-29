package com.svistoyanov.mj.api.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.svistoyanov.mj.api.dto.AbstractEntityDto;

import java.util.UUID;

public class UserDto extends AbstractEntityDto
{
    @JsonProperty("email")
    private String email;

    @JsonProperty("password")
    private String password;

    @JsonProperty("messagesCount")
    private long messagesCount;

    public UserDto()
    {
    }

    public UserDto(UUID id, String email, String password, long messagesCount)
    {
        super(id);
        this.email = email;
        this.password = password;
        this.messagesCount = messagesCount;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public long getMessagesCount()
    {
        return messagesCount;
    }

    public void setMessagesCount(long messagesCount)
    {
        this.messagesCount = messagesCount;
    }
}
