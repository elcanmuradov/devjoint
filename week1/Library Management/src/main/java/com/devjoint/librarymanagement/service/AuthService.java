package com.devjoint.librarymanagement.service;

import com.devjoint.librarymanagement.dto.auth.AuthResponse;
import com.devjoint.librarymanagement.dto.auth.LoginRequest;
import com.devjoint.librarymanagement.dto.auth.RegisterRequest;
import com.devjoint.librarymanagement.dto.user.UserDto;
import com.devjoint.librarymanagement.entity.RefreshToken;
import com.devjoint.librarymanagement.entity.User;
import com.devjoint.librarymanagement.enums.Role;
import com.devjoint.librarymanagement.exception.AuthException;
import com.devjoint.librarymanagement.repository.RefreshTokenRepository;
import com.devjoint.librarymanagement.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthResponse register(@Valid RegisterRequest registerRequest) {

        if (userRepository.existsUserByEmail(registerRequest.getEmail())) {
            throw new AuthException("Email already exists");
        }
        User user = User.builder()
                .fullName(registerRequest.getFullName())
                .email(registerRequest.getEmail())
                .birthDate(registerRequest.getBirthDate())
                .role(Role.USER)
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .build();

        user = userRepository.save(user);

        return getAuthResponse(user);

    }

    public AuthResponse login(@Valid LoginRequest loginRequest) {
        var optional = userRepository.findUserByEmail(loginRequest.getGmail());
        if (optional.isEmpty()) {
            throw new AuthException("Invalid email or password");
        }

        User user = optional.get();
        if (!passwordEncoder.matches(loginRequest.getPassword(),user.getPassword())){
            throw new AuthException("Invalid password");
        }

        return getAuthResponse(user);

    }

    public AuthResponse refreshToken(String refreshToken) {
        var optional = refreshTokenRepository.findRefreshTokenByToken(refreshToken);
        if (optional.isEmpty()) {
            throw new AuthException("Invalid refresh token");
        }

        RefreshToken token = optional.get();

        if(token.getExpiryDate().isBefore(Instant.now())) {
            throw new AuthException("Token has expired");
        }

        if (token.isRevoked()){
            throw new AuthException("Token has been revoked");
        }

        return AuthResponse.builder()
                .accessToken(jwtService.generateToken(token.getUser()))
                .refreshToken(refreshToken)
                .user(userToUserDto(token.getUser()))
                .build();


    }

    private AuthResponse getAuthResponse(User user) {
        String accessToken = jwtService.generateToken(user);

        RefreshToken refreshToken = RefreshToken.builder()
                .token(jwtService.generateRefreshToken(user.getEmail()))
                .user(user)
                .expiryDate(Instant.now().plus(7, ChronoUnit.DAYS))
                .isRevoked(false)
                .build();

        refreshToken =  refreshTokenRepository.save(refreshToken);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .user(userToUserDto(user))
                .build();
    }

    private UserDto userToUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .birthDate(user.getBirthDate())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    public Void logout(String token) {
        UUID userId = jwtService.getUserIdFromToken(token);
        var optional = refreshTokenRepository.findRefreshTokenByUserId(userId);
        if (optional.isEmpty()) {
            throw new AuthException("Invalid token");
        }

        RefreshToken refreshToken = optional.get();
        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);

        return null;
    }
}
