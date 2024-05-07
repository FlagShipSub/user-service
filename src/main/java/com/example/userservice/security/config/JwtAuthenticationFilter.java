package com.example.userservice.security.config;

import com.example.userservice.AuthConstants;
import com.example.userservice.repository.TokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final UserDetailsService userDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (isRegistrationRequest(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");

        if (isInvalidAuthHeader(authHeader)) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = extractJwt(authHeader);
        final String username = jwtService.extractUsername(jwt);

        if (isValidUsername(username)
                && isUserNotAuthenticated()
        ) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (isTokenValid(jwt, userDetails)) {
                setAuthenticationContext(request, userDetails);
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean isRegistrationRequest(HttpServletRequest request) {
        return request.getServletPath().contains(AuthConstants.REGISTRATION_PATH);
    }

    private boolean isInvalidAuthHeader(String authHeader) {
        return authHeader == null || !authHeader.startsWith(AuthConstants.BEARER_PREFIX);
    }

    private String extractJwt(String authHeader) {
        return authHeader.substring(AuthConstants.BEARER_PREFIX.length());
    }

    private boolean isValidUsername(String username) {
        return username != null;
    }

    private boolean isUserNotAuthenticated() {
        return SecurityContextHolder.getContext().getAuthentication() == null;
    }

    private boolean isTokenValid(String jwt, UserDetails userDetails) {
        return jwtService.isTokenValid(jwt, userDetails)
                && isTokenNotExpiredAndNotRevoked(jwt);
    }

    private boolean isTokenNotExpiredAndNotRevoked(String jwt) {
        return tokenRepository.findByToken(jwt)
                .map(t -> !t.isExpired() && !t.isRevoked())
                .orElse(false);
    }

    private void setAuthenticationContext(HttpServletRequest request, UserDetails userDetails) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }
}

