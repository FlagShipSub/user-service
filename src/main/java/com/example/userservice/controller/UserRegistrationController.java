package com.example.userservice.controller;

import com.example.userservice.dto.UserLoginRequest;
import com.example.userservice.dto.UserLoginResponse;
import com.example.userservice.dto.UserRegistrationRequestDTO;
import com.example.userservice.dto.UserRegistrationResponseDTO;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.service.UserRegistrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Check if a username is available",
            description = "Returns true if the username is available, false if already taken"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Username availability checked successfully")
    })
    @GetMapping(value = "/check-username")
    public ResponseEntity<Boolean> checkUsernameAvailability(@RequestParam String username) {
        return ResponseEntity.ok(
                !userRegistrationService.isUserNameAlreadyExists(username)
        );
    }

    @Operation(summary = "Check if an email is available", description = "Returns true if the email is available, false if already registered")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Email availability checked successfully")
    })
    @GetMapping(value = "/check-email")
    public ResponseEntity<Boolean> checkEmailAvailability(@RequestParam String email) {
        return ResponseEntity.ok(
                !userRegistrationService.isEmailAlreadyExists(email)
        );
    }

    @Operation(summary = "Register a new user", description = "Registers a new user in the system")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request, registration failed")
    })
    @PostMapping(value = "/register")
    public ResponseEntity<UserRegistrationResponseDTO> registerUser(
            @RequestBody UserRegistrationRequestDTO registrationRequestDTO) {

        var responseDto = userRegistrationService.registerUser(registrationRequestDTO);
        return responseDto == null ?
                new ResponseEntity<>(HttpStatus.BAD_REQUEST) :
                new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @Operation(summary = "Authenticate a user", description = "Logs in a user with username and password")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Authentication successful"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, invalid credentials")
    })
    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> authenticate(
            @RequestBody UserLoginRequest request
    ) {
        return ResponseEntity.ok(userRegistrationService.authenticate(request));
    }
}
