package com.devjoint.librarymanagement.dto.auth;

import com.devjoint.librarymanagement.dto.user.UserDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {

        private String accessToken;

        private String refreshToken;

        private UserDto user;

}
