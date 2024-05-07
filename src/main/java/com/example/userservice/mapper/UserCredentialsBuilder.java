package com.example.userservice.mapper;

import com.example.userservice.entity.RegisteredUserCredentials;
import com.example.userservice.security.PasswordEncoderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserCredentialsBuilder {
    private final PasswordEncoderService passwordEncoderService;

    public RegisteredUserCredentials buildRegisteredUserCredentials(String password) {
        return RegisteredUserCredentials.builder()
                .password(passwordEncoderService.encodePassword(password))
                .build();
    }
}
