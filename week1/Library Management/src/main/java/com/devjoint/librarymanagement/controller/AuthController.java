package com.devjoint.librarymanagement.controller;

import com.devjoint.librarymanagement.dto.ApiResponse;
import com.devjoint.librarymanagement.dto.auth.AuthResponse;
import com.devjoint.librarymanagement.dto.auth.LoginRequest;
import com.devjoint.librarymanagement.dto.auth.RegisterRequest;
import com.devjoint.librarymanagement.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@RequestBody @Valid RegisterRequest registerRequest) {
        return ResponseEntity.status(201).body(ApiResponse.success(authService.register(registerRequest)));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody @Valid LoginRequest loginRequest) {
        return ResponseEntity.status(200).body(ApiResponse.success(authService.login(loginRequest)));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refresh(@RequestParam String refreshToken) {
        return ResponseEntity.status(200).body(ApiResponse.success(authService.refreshToken(refreshToken)));
    }

    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Void>> logout(@RequestHeader("Authorization") String token) {
        return ResponseEntity.status(204).body(ApiResponse.success(authService.logout(token)));
    }


}
