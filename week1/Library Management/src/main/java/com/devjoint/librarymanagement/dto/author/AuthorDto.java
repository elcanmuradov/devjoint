package com.devjoint.librarymanagement.dto.author;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class AuthorDto {
    private UUID id;

    private String fullName;

    private LocalDate birthDate;

    private String description;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
