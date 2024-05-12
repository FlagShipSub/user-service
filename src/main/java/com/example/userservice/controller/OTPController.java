package com.example.userservice.controller;

import com.example.userservice.dto.OTPGenerationRequest;
import com.example.userservice.dto.OTPVerificationRequest;
import com.example.userservice.service.OTP.IOTPGenerationService;
import com.example.userservice.service.OTP.IOTPVerificationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping(path = "api/v1/otp")
public class OTPController {

    private IOTPGenerationService otpGenerationService;

    private IOTPVerificationService otpVerificationService;

    @PostMapping("/generate")
    public ResponseEntity<String> generateOTP(@RequestBody OTPGenerationRequest requestBody) {
        otpGenerationService.generateOTP(requestBody.email);
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
