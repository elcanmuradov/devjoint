package com.devjoint.librarymanagement.controller;

import com.devjoint.librarymanagement.dto.ApiResponse;
import com.devjoint.librarymanagement.dto.genre.GenreDto;
import com.devjoint.librarymanagement.dto.genre.GenreRequest;
import com.devjoint.librarymanagement.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/genre")
public class GenreController {

    private final GenreService genreService;

    @PostMapping()
    public ResponseEntity<ApiResponse<GenreDto>> addGenre(@RequestBody GenreRequest request) {
        return ResponseEntity.ok(ApiResponse.success(genreService.create(request)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<GenreDto>> update(@PathVariable UUID id,@RequestBody GenreRequest request) {
        return ResponseEntity.ok(ApiResponse.success(genreService.update(id,request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponse.success(genreService.delete(id)));
    }



}
