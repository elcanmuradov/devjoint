package com.devjoint.librarymanagement.dto.author;

import com.devjoint.librarymanagement.enums.Genre;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AuthorRequest {
    @Size(min = 3, max = 100)
    private String fullName;

    @NotNull
    private LocalDate birthDate;

    @NotNull
    @NotBlank
    private String description;

}
