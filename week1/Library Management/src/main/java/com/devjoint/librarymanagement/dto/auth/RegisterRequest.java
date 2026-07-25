package com.devjoint.librarymanagement.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RegisterRequest {
    @NotNull
    @NotBlank
    private String fullName;

    @Email
    private String email;

    @NotNull
    private LocalDate birthDate;

    @NotNull
    private String password;
}
