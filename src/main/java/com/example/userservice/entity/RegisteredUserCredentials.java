package com.example.userservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisteredUserCredentials {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String password;

}
