package com.devjoint.librarymanagement.service;

import com.devjoint.librarymanagement.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm; // ✅ BU IMPORTU ƏLAVƏ EDİN
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${spring.security.secret-key}")
    private String secretKey;

    public String generateToken(User user) {
        return Jwts.builder()
                .subject(user.getEmail())
                .claim("userId", user.getId())
                .claim("role", user.getRole())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 15))
                .signWith(getSecretKey(), SignatureAlgorithm.HS256) // ✅ Algorithm əlavə edildi
                .compact();
    }

    public String getUsernameFromToken(String token) {
        try {
            String extractedToken = extractToken(token);

            if (extractedToken == null || extractedToken.isEmpty()) {
                throw new IllegalArgumentException("Token is null or empty");
            }

            return Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(extractedToken)
                    .getPayload()
                    .getSubject();
        } catch (Exception e) {
            throw new RuntimeException("Invalid JWT token: " + e.getMessage(), e);
        }
    }

    private <T> T exportToken(String token, Function<Claims, T> function) {
        try {
            String extractedToken = extractToken(token);

            final var claims = Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(extractedToken)
                    .getPayload();

            return function.apply(claims);
        } catch (Exception e) {
            throw new RuntimeException("Invalid JWT token: " + e.getMessage(), e);
        }
    }

    public boolean tokenControl(String jwt, UserDetails userDetails) {
        try {
            final String username = getUsernameFromToken(jwt);
            final Date expiration = exportToken(jwt, Claims::getExpiration);
            return username.equals(userDetails.getUsername()) && !expiration.before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public String generateRefreshToken(String email) {
        return UUID.randomUUID().toString();
    }

    public String getUserIdFromToken(String token) {
        try {
            String extractedToken = extractToken(token);

            return Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(extractedToken)
                    .getPayload()
                    .get("userId", String.class);
        } catch (Exception e) {
            throw new RuntimeException("Invalid JWT token: " + e.getMessage(), e);
        }
    }

    public String extractToken(String token) {
        if (token == null) {
            return null;
        }
        if (token.startsWith("Bearer ")) {
            return token.substring(7).trim();
        }
        return token.trim();
    }

    private SecretKey getSecretKey() {
        if (secretKey == null || secretKey.getBytes(StandardCharsets.UTF_8).length < 32) {
            throw new IllegalArgumentException("Secret key must be at least 32 characters long");
        }
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }
}