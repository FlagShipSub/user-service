package com.example.userservice.service.OTP;

public interface IOTPVerificationService {

    public boolean verifyOTP(String email, Integer otp);
}
