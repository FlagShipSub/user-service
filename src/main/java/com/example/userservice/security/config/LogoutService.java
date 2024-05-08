package com.example.userservice.security.config;

import com.example.userservice.AuthConstants;
import com.example.userservice.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final TokenRepository tokenRepository;

    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        final String authHeader = request.getHeader(AuthConstants.AUTHORIZATION_HEADER);
        final String jwt;

        if (StringUtils.hasText(authHeader) &&
                authHeader.startsWith(AuthConstants.BEARER_PREFIX)
        ) {
            jwt = authHeader.substring(
                    AuthConstants
                    .BEARER_PREFIX
                            .length()
            );
            var storedToken = tokenRepository.findByToken(jwt)
                    .orElseGet(() -> null);

            if (storedToken != null) {
                storedToken.setExpired(true);
                storedToken.setRevoked(true);
                tokenRepository.save(storedToken);
                SecurityContextHolder.clearContext();
            }

        }
    }
}
