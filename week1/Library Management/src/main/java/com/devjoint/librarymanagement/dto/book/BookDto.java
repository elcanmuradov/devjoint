package com.devjoint.librarymanagement.dto.book;

import com.devjoint.librarymanagement.dto.genre.GenreDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDto {

    private UUID id;

    private String title;

    private UUID authorId;

    private Set<GenreDto> genres;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
