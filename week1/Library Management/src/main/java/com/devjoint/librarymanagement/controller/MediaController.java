package com.devjoint.librarymanagement.controller;

import com.devjoint.librarymanagement.dto.ApiResponse;
import com.devjoint.librarymanagement.service.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.print.attribute.standard.Media;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/media")
public class MediaController {
    private final MediaService mediaService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<String>> uploadFile(@RequestParam("file") MultipartFile file) {
        String savedPath = mediaService.save(file);
        return ResponseEntity.ok(ApiResponse.success("Fayl saxlanıldı: " + savedPath));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Resource>> getFile(@RequestParam("fileName") String fileName) {
        return ResponseEntity.ok(ApiResponse.success(mediaService.load(fileName)));
    }

}
