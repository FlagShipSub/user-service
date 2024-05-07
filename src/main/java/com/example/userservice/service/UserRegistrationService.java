package com.example.userservice.service;

import com.example.userservice.dto.UserLoginRequest;
import com.example.userservice.dto.UserLoginResponse;
import com.example.userservice.dto.UserRegistrationRequestDTO;
import com.example.userservice.dto.UserRegistrationResponseDTO;
import com.example.userservice.entity.Token;
import com.example.userservice.entity.TokenType;
import com.example.userservice.entity.User;
import com.example.userservice.mapper.UserBuilder;
import com.example.userservice.repository.TokenRepository;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.security.config.JwtService;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserRegistrationService implements UserDetailsService {
    private static final Logger log = LoggerFactory.getLogger(UserRegistrationService.class);
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService JwtTokenProvider;
    private final TokenRepository tokenRepository;
    private final EntityManager entityManager;
    private final UserBuilder userBuilder;


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


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Loading user by username: {}", username);
        return userRepository
                .findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
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

        var user = loadUserByUsername(request.getUserName());

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
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

//    public List<Token> findAllValidTokenByUser(String userId) {
//        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//        CriteriaQuery<Token> query = cb.createQuery(Token.class);
//        Root<Token> tokenRoot = query.from(Token.class);
//
//        Join<Token, User> userJoin = tokenRoot.join("user");
//        Predicate userIdPredicate = cb.equal(userJoin.get("id"), userId);
//        Predicate tokenValidPredicate = cb.or(
//                cb.equal(tokenRoot.get("expired"), false),
//                cb.equal(tokenRoot.get("revoked"), false)
//        );
//
//        query.select(tokenRoot)
//                .where(userIdPredicate, tokenValidPredicate);
//
//        return entityManager.createQuery(query).getResultList();
//    }

}