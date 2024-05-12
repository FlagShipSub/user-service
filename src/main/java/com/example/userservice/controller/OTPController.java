package com.example.userservice.controller;

import com.example.userservice.dto.OTPGenerationRequest;
import com.example.userservice.dto.OTPVerificationRequest;
import com.example.userservice.service.EmailService;
import com.example.userservice.service.OTPGenerationService;
import com.example.userservice.service.OTPVerificationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping(path = "api/v1/otp")
public class OTPController {

    private EmailService emailService;

    private OTPGenerationService otpGenerationService;

    private OTPVerificationService otpVerificationService;

    @PostMapping("/generate")
    public ResponseEntity<String> generateOTP(@RequestBody OTPGenerationRequest requestBody) {
        Integer otp = otpGenerationService.generateOTP(requestBody.email);
        emailService.sendOtpMessage(requestBody.email, otp);

        return ResponseEntity.ok("OTP sent to the user!");
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyOTP(@RequestBody OTPVerificationRequest requestBody) {
        boolean isValid = otpVerificationService.verifyOTP(requestBody.email, requestBody.otp);
        if (isValid) {
            return ResponseEntity.ok("OTP is valid.");
        } else {
            return ResponseEntity.badRequest().body("Invalid OTP or OTP expired.");
        }
    }
}
