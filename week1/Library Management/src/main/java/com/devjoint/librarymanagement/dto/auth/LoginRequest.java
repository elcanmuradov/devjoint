package com.devjoint.librarymanagement.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginRequest {

    @Email
    private String gmail;

    @NotNull
    @NotEmpty
    private String password;

}
