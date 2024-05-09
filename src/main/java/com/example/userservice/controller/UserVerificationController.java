package com.example.userservice.controller;

import com.example.userservice.dto.UserVerificationRequest;
import com.example.userservice.service.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping(path = "api/v1")
public class UserVerificationController {

    private EmailService emailService;

    @PostMapping("/verify")
    public ResponseEntity<String> verify(
            @RequestBody UserVerificationRequest requestBody
    ) {
        emailService.sendOtpMessage("rithesh.441@gmail.com", 123456);
        Map<String, String> data = new HashMap<>();
        data.put("recipient", "rithesh.441@gmail.com");
        data.put("username", "Rithesh");
        emailService.sendHtmlMail(data);
        return ResponseEntity.ok("OTP Sent!");
    }
}
