package com.devjoint.librarymanagement.controller;

import com.devjoint.librarymanagement.dto.ApiResponse;
import com.devjoint.librarymanagement.dto.author.AuthorDto;
import com.devjoint.librarymanagement.dto.author.AuthorRequest;
import com.devjoint.librarymanagement.dto.book.BookDto;
import com.devjoint.librarymanagement.service.AuthorService;
import com.devjoint.librarymanagement.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/author")
public class AuthorController {
    private final AuthorService authorService;
    private final BookService bookService;

    @Operation(summary = "Create author", description = "Create author via name, birthdate, and  it's own description")
    @PostMapping()
    public ResponseEntity<ApiResponse<AuthorDto>> addAuthor(@RequestBody @Valid AuthorRequest request){
        return ResponseEntity.status(201).body(ApiResponse.success(authorService.createAuthor(request)));
    }

    @Operation(summary = "Update user by id")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AuthorDto>> updateAuthor(@Parameter(description = "User's id must be UUID", examples = @ExampleObject(value = "88cd0def-561c-481c-94c5-ca432d6dcaa")) @PathVariable UUID id, @RequestBody AuthorRequest request){
        return ResponseEntity.status(200).body(ApiResponse.success(authorService.updateAuthor(id, request)));
    }

    @Operation(summary ="Delete author via id")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAuthor(@Parameter(description = "User's id must be UUID", examples = @ExampleObject(value = "88cd0def-561c-481c-94c5-ca432d6dcaa")) @PathVariable UUID id){
       return ResponseEntity.status(200).body(ApiResponse.success(authorService.deleteAuthor(id)));
    }

    @GetMapping()
    @Operation(summary = "Get all authors")
    public ResponseEntity<ApiResponse<Page<AuthorDto>>> getAllAuthors(@PageableDefault(size = 20) Pageable pageable){
        return  ResponseEntity.status(200).body(ApiResponse.success(authorService.getAllAuthors(pageable)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get author by id")
    public ResponseEntity<ApiResponse<AuthorDto>> getAuthorById(@PathVariable UUID id){
        return ResponseEntity.status(200).body(ApiResponse.success(authorService.getAuthor(id)));
    }

    @Operation(summary = "Get author's books")
    @GetMapping("/{id}/books")
    public ResponseEntity<ApiResponse<Page<BookDto>>> getBooksByAuthorId(@PathVariable UUID id, @PageableDefault(page = 0, size = 20) Pageable pageable){
        return ResponseEntity.status(200).body(ApiResponse.success(bookService.getBooksByAuthorId(id,pageable)));
    }
}
