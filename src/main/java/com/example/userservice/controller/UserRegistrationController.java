package com.example.userservice.controller;

import com.example.userservice.dto.UserLoginRequest;
import com.example.userservice.dto.UserLoginResponse;
import com.example.userservice.dto.UserRegistrationRequestDTO;
import com.example.userservice.dto.UserRegistrationResponseDTO;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.service.UserRegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping(path = "api/v1/registration")
public class UserRegistrationController {

    final UserRepository userRepository;
    final UserRegistrationService userRegistrationService;


    @GetMapping(value = "/check-username")
    public ResponseEntity<Boolean> checkUsernameAvailability(@RequestParam String username) {
        return ResponseEntity.ok(
                !userRegistrationService.isUserNameAlreadyExists(username)
        );
    }

    @GetMapping(value = "/check-email")
    public ResponseEntity<Boolean> checkEmailAvailability(@RequestParam String email) {
        return ResponseEntity.ok(
                !userRegistrationService.isEmailAlreadyExists(email)
        );
    }


    @PostMapping(value = "/register")
    public ResponseEntity<UserRegistrationResponseDTO> registerUser(
            @RequestBody UserRegistrationRequestDTO registrationRequestDTO) {

        var responseDto = userRegistrationService.registerUser(registrationRequestDTO);
        return responseDto == null ?
                new ResponseEntity<>(HttpStatus.BAD_REQUEST) :
                new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> authenticate(
            @RequestBody UserLoginRequest request
    ) {
        return ResponseEntity.ok(userRegistrationService.authenticate(request));
    }
}
