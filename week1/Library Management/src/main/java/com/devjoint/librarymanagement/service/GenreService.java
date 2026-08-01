package com.devjoint.librarymanagement.service;

import com.devjoint.librarymanagement.dto.genre.GenreDto;
import com.devjoint.librarymanagement.dto.genre.GenreRequest;
import com.devjoint.librarymanagement.entity.Author;
import com.devjoint.librarymanagement.entity.Genre;
import com.devjoint.librarymanagement.exception.NotFoundException;
import com.devjoint.librarymanagement.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreRepository genreRepository;

    public GenreDto create(GenreRequest request) {

        Genre genre = Genre.builder()
                .name(request.getName())
                .books(new HashSet<>())
                .description(request.getDescription())
                .build();

        genre = genreRepository.save(genre);

        return  genreToDto(genre);
    }

    public GenreDto update(UUID id, GenreRequest request) {
        var genre = genreRepository.findGenreById(id).orElseThrow(()->new NotFoundException("Genre not found"));

        genre.setName(request.getName());
        genre.setDescription(request.getDescription());
        genre = genreRepository.save(genre);

        return  genreToDto(genre);
    }

    public Void delete(UUID id) {
        genreRepository.deleteById(id);
        return null;
    }

    private GenreDto genreToDto(Genre genre) {
        return GenreDto.builder()
                .id(genre.getId())
                .name(genre.getName())
                .description(genre.getDescription())
                .build();
    }
}
