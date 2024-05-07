package com.example.userservice.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UserRegistrationRequestDTO {
    private String userName;
    private String email;
    private String password;
    private String phoneNumber;
    private String city;
    private String state;
    private String country;
    private String gender;
    private boolean isAdmin;
}
