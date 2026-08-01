package com.devjoint.librarymanagement.repository;

import com.devjoint.librarymanagement.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface GenreRepository extends JpaRepository<Genre, UUID> {

    Optional<Genre> findGenreById(UUID uuid);

}
