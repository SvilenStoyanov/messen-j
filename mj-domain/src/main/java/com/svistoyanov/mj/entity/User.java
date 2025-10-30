package com.svistoyanov.mj.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

import java.util.UUID;

import static com.svistoyanov.mj.constants.Constants.*;

public class User extends AbstractEntity {
    //    protected UUID id;

    @NotNull(message = EMAIL_NULL_ERROR_MESSAGE)
    @Email(regexp = EMAIL_PATTERN, message = EMAIL_PATTERN_ERROR_MESSAGE)
    private String email;

    @NotNull(message = USERNAME_NULL_ERROR_MESSAGE)
    @Length(min = 3, max = 20, message = USERNAME_LENGTH_ERROR_MESSAGE)
    @Pattern(regexp = USERNAME_PATTERN, message = USERNAME_PATTERN_ERROR_MESSAGE)
    private String username;

    // String - Encrypted with an algorithm chosen by you
    @NotBlank(message = PASSWORD_EMPTY_ERROR_MESSAGE)
    @Length(min = 3, max = 20, message = PASSWORD_LENGTH_ERROR_MESSAGE)
    private String password;

    protected User() {
        super();
    }

    public User(UUID id) {
        super(id);
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
