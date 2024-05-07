package com.example.userservice.security;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PasswordEncoderService {
    private PasswordEncoder bCryptPasswordEncoder;

    /*
     * This method is used to encode the rawPassword
     * @param rawPassword
     */
    public String encodePassword(String rawPassword) {
        return bCryptPasswordEncoder
                .encode(rawPassword);
    }
}
