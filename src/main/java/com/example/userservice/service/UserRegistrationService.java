package com.example.userservice.service;

import com.example.userservice.dto.UserLoginRequest;
import com.example.userservice.dto.UserLoginResponse;
import com.example.userservice.dto.UserRegistrationRequestDTO;
import com.example.userservice.dto.UserRegistrationResponseDTO;
import com.example.userservice.entity.Token;
import com.example.userservice.entity.TokenType;
import com.example.userservice.entity.User;
import com.example.userservice.mapper.UserBuilder;
import com.example.userservice.repository.TokenCustomRepository;
import com.example.userservice.repository.TokenRepository;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.security.config.JwtService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserRegistrationService {
    private static final Logger log = LoggerFactory.getLogger(UserRegistrationService.class);
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService JwtTokenProvider;
    private final TokenRepository tokenRepository;
    private final UserBuilder userBuilder;
    private final UserDetailsService userDetailsService;
    private final TokenCustomRepository tokenCustomRepository;


    public boolean isUserNameAlreadyExists(String userName) {
        log.debug("Checking if username exists: {}", userName);
        return userRepository
                .findByUserName(userName)
                .isPresent();
    }


    public boolean isEmailAlreadyExists(String email) {
        log.debug("Checking if email exists: {}", email);
        return userRepository
                .findByEmail(email)
                .isPresent();
    }


    public UserRegistrationResponseDTO registerUser(UserRegistrationRequestDTO userRegistrationRequestDTO) {
        log.debug("Registering new user with DTO: {}", userRegistrationRequestDTO);
        User user = userBuilder.buildUser(userRegistrationRequestDTO);
        User savedUser = userRepository.save(user);
        return new UserRegistrationResponseDTO(savedUser);
    }

    public UserLoginResponse authenticate(UserLoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUserName(),
                        request.getPassword()
                )
        );

        var user = userDetailsService.loadUserByUsername(request.getUserName());

        var jwtToken = JwtTokenProvider.generateToken(user);

        var refreshToken = JwtTokenProvider.generateRefreshToken(user);

        revokeAllUserTokens((User) user);

        saveUserToken(user, jwtToken);

        return new UserLoginResponse(jwtToken, refreshToken);

    }

    private void saveUserToken(UserDetails user, String jwtToken) {
        var token = Token.builder()
                .user((User) user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .isExpired(false)
                .isRevoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenCustomRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

}
