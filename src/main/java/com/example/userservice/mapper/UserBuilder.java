package com.example.userservice.mapper;

import com.example.userservice.dto.UserRegistrationRequestDTO;
import com.example.userservice.entity.Gender;
import com.example.userservice.entity.Roles;
import com.example.userservice.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserBuilder {
    private UserAddressBuilder userAddressBuilder;
    private UserCredentialsBuilder userCredentialsBuilder;

    public User buildUser(UserRegistrationRequestDTO requestDTO) {
        return User.builder()
                .userName(requestDTO.getUserName())
                .email(requestDTO.getEmail())
                .phoneNumber(requestDTO.getPhoneNumber())
                .userAddress(userAddressBuilder.buildUserAddress(requestDTO))
                .gender(getGender(requestDTO.getGender()))
                .isRemoved(false)
                .isEnabled(true)
                .rolesAssigned(getRoles(requestDTO.isAdmin()))
                .registeredUserCredentials(userCredentialsBuilder.buildRegisteredUserCredentials(requestDTO.getPassword()))
                .build();
    }

    private Gender getGender(String gender) {
        return "MALE".equalsIgnoreCase(gender) ? Gender.MALE : Gender.FEMALE;
    }

    private Roles getRoles(boolean isAdmin) {
        return isAdmin ? Roles.ADMIN : Roles.USER;
    }
}
