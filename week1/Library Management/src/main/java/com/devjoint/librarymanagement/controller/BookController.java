package com.devjoint.librarymanagement.controller;

import com.devjoint.librarymanagement.dto.ApiResponse;
import com.devjoint.librarymanagement.dto.book.BookDto;
import com.devjoint.librarymanagement.dto.book.BookRequest;
import com.devjoint.librarymanagement.entity.Book;
import com.devjoint.librarymanagement.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<BookDto>> addBook(@RequestBody @Valid BookRequest request){
        return ResponseEntity.status(201).body(ApiResponse.success(bookService.createBook(request)));
    }

    @Operation(summary = "Update book", description = "Update book via authorId, title , and genre")
    @PutMapping("/{id}/update")
    public ResponseEntity<ApiResponse<BookDto>> updateBook(@PathVariable UUID id, @RequestBody BookRequest request){
        return ResponseEntity.status(200).body(ApiResponse.success(bookService.updateBook(id, request)));
    }

    @Operation(summary = "Delete book via id ")
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<ApiResponse<Void>> deleteBook(@PathVariable UUID id){
        return ResponseEntity.status(200).body(ApiResponse.success(bookService.deleteBook(id)));
    }

    @Operation(summary = "Get all books")
    @GetMapping()
    public ResponseEntity<ApiResponse<Page<BookDto>>> getAllBooks(Integer page, Integer pageSize){
        return ResponseEntity.status(200).body(ApiResponse.success(bookService.getAllBooks(page,pageSize)));
    }

    @Operation(summary = "Get book via id ")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookDto>> getBookById(@PathVariable UUID id){
        return ResponseEntity.status(200).body(ApiResponse.success(bookService.getBookById(id)));
    }



}
