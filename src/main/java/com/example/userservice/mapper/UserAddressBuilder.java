package com.example.userservice.mapper;

import com.example.userservice.dto.UserRegistrationRequestDTO;
import com.example.userservice.entity.UserAddress;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class UserAddressBuilder {

    public UserAddress buildUserAddress(UserRegistrationRequestDTO requestDTO) {
        return UserAddress.builder()
                .city(requestDTO.getCity())
                .state(requestDTO.getState())
                .country(Optional.ofNullable(requestDTO.getCountry()).orElse("INDIA"))
                .build();
    }
}
