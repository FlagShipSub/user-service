package com.example.userservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class OTP {
    @Id
    @GeneratedValue
    public Long id;

    public Integer otpValue;

    @Column(unique = true)
    private String email;

    public LocalDateTime expiryTime;

    public boolean isVerified;
}