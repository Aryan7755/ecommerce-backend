package com.aryan.ecommerce_backend.auth.service;


import com.aryan.ecommerce_backend.auth.dto.AuthResponse;
import com.aryan.ecommerce_backend.auth.dto.LoginRequest;
import com.aryan.ecommerce_backend.auth.dto.RegisterRequest;
import com.aryan.ecommerce_backend.exception.BadRequestException;
import com.aryan.ecommerce_backend.security.jwt.JwtService;
import com.aryan.ecommerce_backend.user.entity.Role;
import com.aryan.ecommerce_backend.user.entity.User;
import com.aryan.ecommerce_backend.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtService jwtService;
    @Mock private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    @Test
    void register_success_returnsTokens() {
        RegisterRequest request = new RegisterRequest("Test User", "test@example.com", "password123");

        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("hashed-password");
        when(jwtService.generateAccessToken(any())).thenReturn("access-token");
        when(jwtService.generateRefreshToken(any())).thenReturn("refresh-token");

        AuthResponse response = authService.register(request);

        assertEquals("access-token", response.accessToken());
        assertEquals("refresh-token", response.refreshToken());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_duplicateEmail_throwsBadRequestException() {
        RegisterRequest request = new RegisterRequest("Test User", "existing@example.com", "password123");

        when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);

        assertThrows(BadRequestException.class, () -> authService.register(request));
        verify(userRepository, never()).save(any());
    }

    @Test
    void login_success_returnsTokens() {
        LoginRequest request = new LoginRequest("test@example.com", "password123");
        User user = User.builder()
                .email("test@example.com")
                .role(Role.USER)
                .password("hashed-password")
                .build();

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(jwtService.generateAccessToken(any())).thenReturn("access-token");
        when(jwtService.generateRefreshToken(any())).thenReturn("refresh-token");

        AuthResponse response = authService.login(request);

        assertEquals("access-token", response.accessToken());
        verify(authenticationManager).authenticate(any());
    }
}
