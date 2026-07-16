package com.devjoint.librarymanagement.dto.book;

import com.devjoint.librarymanagement.enums.Genre;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDto {

    private UUID id;

    private String title;

    private UUID authorId;

    private Genre genre;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
