package com.devjoint.librarymanagement.dto.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class BookRequest {
    @NotNull
    @NotBlank
    private String title;

    @NotNull
    private UUID authorId;

    @NotNull
    private Genre genre;
}
