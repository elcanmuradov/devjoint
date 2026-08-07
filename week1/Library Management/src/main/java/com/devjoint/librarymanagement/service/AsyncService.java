package com.devjoint.librarymanagement.service;

import com.devjoint.librarymanagement.dto.user.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Async
public class AsyncService {

        public void sendWelcomeMessage(UserDto userDto) {
            log.info(userDto.getEmail() +  " Welcome to library Management Service!");
        }

}
