package com.devjoint.librarymanagement.repository;

import com.devjoint.librarymanagement.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AuthorRepository extends JpaRepository<Author, UUID> {
}
