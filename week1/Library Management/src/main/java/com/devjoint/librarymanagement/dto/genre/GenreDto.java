package com.devjoint.librarymanagement.dto.genre;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class GenreDto {
    private UUID id;

    private String name;

    private String description;

  }
