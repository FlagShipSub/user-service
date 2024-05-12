package com.example.userservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Value("${EMAIL_SENDER}")
    public String sender;

    public String getSender() {
        return sender;
    }
}
