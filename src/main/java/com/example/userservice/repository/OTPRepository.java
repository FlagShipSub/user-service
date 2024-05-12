package com.example.userservice.repository;

import com.example.userservice.entity.OTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface OTPRepository extends JpaRepository<OTP, Long> {

    Optional<OTP> findByEmailAndOtpValueAndVerifiedIsFalse(String email, Integer otpValue);

    @Modifying
    @Transactional
    @Query("""
            UPDATE      OTP o
            SET         o.otpValue = :#{#otp.otpValue},
                        o.expiryTime = :#{#otp.expiryTime},
                        o.verified = false
            WHERE       o.email = :#{#otp.email}
    """)
    void updateByEmail(OTP otp);
}
