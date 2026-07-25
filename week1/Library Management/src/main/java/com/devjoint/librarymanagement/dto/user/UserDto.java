package com.devjoint.librarymanagement.dto.user;

import com.devjoint.librarymanagement.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private UUID id;

    private String fullName;

    private String email;

    private LocalDate birthDate;

    private Role role;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
