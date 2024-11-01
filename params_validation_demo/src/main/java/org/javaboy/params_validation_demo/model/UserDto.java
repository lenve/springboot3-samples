package org.javaboy.params_validation_demo.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.javaboy.params_validation_demo.group.RegisterGroup;
import org.javaboy.params_validation_demo.group.UpdateGroup;

/**
 * @author：江南一点雨
 * @site：http://www.javaboy.org
 * @微信公众号：江南一点雨
 * @github：https://github.com/lenve
 * @gitee：https://gitee.com/lenve
 */
public class UserDto {
    @NotNull(message = "{NotEmpty.username}", groups = {RegisterGroup.class, UpdateGroup.class})
    private String username;

    @NotBlank(message = "{NotBlank.password}", groups = {RegisterGroup.class})
    private String password;

    @Email(message = "{Email.email}", groups = {RegisterGroup.class, UpdateGroup.class})
    private String email;


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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}