package com.devjoint.librarymanagement.service;

import com.devjoint.librarymanagement.dto.author.AuthorDto;
import com.devjoint.librarymanagement.dto.author.AuthorRequest;
import com.devjoint.librarymanagement.entity.Author;
import com.devjoint.librarymanagement.exception.NotFoundException;
import com.devjoint.librarymanagement.repository.AuthorRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorDto createAuthor(@Valid AuthorRequest request) {
        Author author = Author.builder()
                .fullName(request.getFullName())
                .birthDate(request.getBirthDate())
                .description(request.getDescription())
                .build();

        author = authorRepository.save(author);

        return authorToDto(author);

    }

    public AuthorDto updateAuthor(UUID uuid, @Valid AuthorRequest request) {
        var opt = authorRepository.findById(uuid);
        if (opt.isEmpty()) {
            throw new NotFoundException("Author not found");
        }
        var author = opt.get();

        author.setFullName(request.getFullName());
        author.setBirthDate(request.getBirthDate());
        author.setDescription(request.getDescription());

        author = authorRepository.save(author);

        return authorToDto(author);

    }

    public Void deleteAuthor(UUID uuid) {
        authorRepository.deleteById(uuid);
        return null;
    }

    public List<AuthorDto> getAllAuthors() {
        List<AuthorDto> authors = new ArrayList<>();
        authorRepository.findAll().forEach(author -> authors.add(authorToDto(author)));
        return authors;
    }

    public AuthorDto getAuthor(UUID uuid) {
        return authorToDto(authorRepository.findById(uuid).orElseThrow(() -> new NotFoundException("Author not found")));
    }

    private AuthorDto authorToDto(Author author) {
        return AuthorDto.builder()
                .id(author.getId())
                .fullName(author.getFullName())
                .birthDate(author.getBirthDate())
                .description(author.getDescription())
                .createdAt(author.getCreatedAt())
                .updatedAt(author.getUpdatedAt())
                .build();
    }


}
