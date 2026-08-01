package com.devjoint.librarymanagement.controller;

import com.devjoint.librarymanagement.dto.ApiResponse;
import com.devjoint.librarymanagement.dto.book.BookDto;
import com.devjoint.librarymanagement.dto.book.BookRequest;
import com.devjoint.librarymanagement.dto.genre.GenreRequest;
import com.devjoint.librarymanagement.entity.Book;
import com.devjoint.librarymanagement.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;

    @Operation(summary = "Create book", description = "Create book via authorId, title , and genre")
    @PostMapping()
    public ResponseEntity<ApiResponse<BookDto>> addBook(@RequestBody @Valid BookRequest request){
        return ResponseEntity.status(201).body(ApiResponse.success(bookService.createBook(request)));
    }

    @Operation(summary = "Update book", description = "Update book via authorId, title , and genre")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BookDto>> updateBook(@PathVariable UUID id, @RequestBody BookRequest request){
        return ResponseEntity.status(200).body(ApiResponse.success(bookService.updateBook(id, request)));
    }

    @Operation(summary = "Delete book via id ")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBook(@PathVariable UUID id){
        return ResponseEntity.status(200).body(ApiResponse.success(bookService.deleteBook(id)));
    }

    @Operation(summary = "Get all books")
    @GetMapping()
    public ResponseEntity<ApiResponse<Page<BookDto>>> getAllBooks(@PageableDefault(page = 0, size = 20) Pageable pageable){
        return ResponseEntity.status(200).body(ApiResponse.success(bookService.getAllBooks(pageable)));
    }

    @Operation(summary = "Get book via id ")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookDto>> getBookById(@PathVariable UUID id){
        return ResponseEntity.status(200).body(ApiResponse.success(bookService.getBookById(id)));
    }

    @PostMapping("/{bookId}/genre")
    public ResponseEntity<ApiResponse<BookDto>> addGenre(@PathVariable UUID bookId,@RequestParam UUID genreId){
        return ResponseEntity.ok(ApiResponse.success(bookService.addGenre(bookId,genreId)));
    }



}
