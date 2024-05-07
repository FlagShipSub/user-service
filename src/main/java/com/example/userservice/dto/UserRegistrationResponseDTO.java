package com.example.userservice.dto;

import com.example.userservice.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationResponseDTO {
    private String userName;
    private String email;
    private String role;
    private String phoneNumber;
    private String city;
    private String state;
    private String country;
    private String gender;
    private boolean isRemoved;
    private boolean isEnabled;

    public UserRegistrationResponseDTO(User user) {
        this.userName = user.getUsername();
        this.email = user.getEmail();
        this.role = user.getRolesAssigned().name();
        this.phoneNumber = user.getPhoneNumber();
        this.city = user.getUserAddress().getCity();
        this.state = user.getUserAddress().getState();
        this.country = user.getUserAddress().getCountry();
        this.gender = user.getGender().name();
        this.isRemoved = user.getIsRemoved();
        this.isEnabled = user.getIsEnabled();
    }
}
