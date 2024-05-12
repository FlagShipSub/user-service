package com.example.userservice.service.OTP;

import com.example.userservice.entity.OTP;
import com.example.userservice.repository.OTPRepository;
import com.example.userservice.service.Email.IEmailService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@AllArgsConstructor
@Service
public class OTPGenerationService implements IOTPGenerationService {

    private OTPRepository otpRepository;

    private IEmailService emailService;

    public void generateOTP(String email) {
        // Generate a random 6-digit OTP
        Integer otpValue = generateRandomOTP();

        // Store the OTP in the database
        OTP otpEntity = new OTP();
        otpEntity.setEmail(email);
        otpEntity.setOtpValue(otpValue);
        otpEntity.setExpiryTime(LocalDateTime.now().plusMinutes(5)); // Set expiry time to 5 minutes from now
        otpEntity.setVerified(false); // Set OTP as not verified initially

        emailService.sendOtpMessage(email, otpValue);
        try {
            otpRepository.save(otpEntity);
        } catch (DataIntegrityViolationException e) {
            otpRepository.updateByEmail(otpEntity);
        }
    }

    private Integer generateRandomOTP() {
        Random random = new Random();
        int min = 100000;
        int max = 999999;

        return random.nextInt(max - min + 1) + min;
    }
}
