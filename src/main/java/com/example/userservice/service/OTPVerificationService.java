package com.example.userservice.service;

import com.example.userservice.entity.OTP;
import com.example.userservice.repository.OTPRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@AllArgsConstructor
@Service
public class OTPVerificationService {

    private OTPRepository otpRepository;

    public boolean verifyOTP(String email, Integer otp) {
        Optional<OTP> optionalOTP = otpRepository.findByEmailAndOtpValueAndVerifiedIsFalse(email, otp);

        if (optionalOTP.isPresent()) {
            OTP otpEntity = optionalOTP.get();

            // Check if OTP has expired
            if (otpEntity.getExpiryTime().isBefore(LocalDateTime.now())) {
                // Handle expired OTP
                return false;
            }

            // Mark OTP as verified
            otpEntity.setVerified(true);
            otpRepository.save(otpEntity);

            return true; // OTP verified successfully
        }

        return false; // OTP not found or already verified
    }
}

