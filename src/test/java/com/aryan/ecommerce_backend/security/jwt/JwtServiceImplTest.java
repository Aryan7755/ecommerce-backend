package com.aryan.ecommerce_backend.security.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;

public class JwtServiceImplTest {
    private JwtServiceImpl jwtService;
    private UserDetails testUser;

    @BeforeEach
    void setUp() {
        JwtProperties properties = new JwtProperties();
        properties.setSecret("test-secret-key-for-unit-tests-must-be-long-enough-for-hs512-algorithm-signing");
        properties.setAccessTokenExpiration(900000L);
        properties.setRefreshTokenExpiration(604800000L);

        jwtService = new JwtServiceImpl(properties);
        testUser = new User("test@example.com", "password", java.util.List.of());
    }

    @Test
    void generateAccessToken_createsValidToken() {
        String token = jwtService.generateAccessToken(testUser);

        assertNotNull(token);
        assertTrue(token.startsWith("eyJ"));
        assertEquals(2, token.chars().filter(c -> c == '.').count());
    }

    @Test
    void extractUsername_returnsCorrectEmail() {
        String token = jwtService.generateAccessToken(testUser);

        String extractedUsername = jwtService.extractUsername(token);

        assertEquals("test@example.com", extractedUsername);
    }

    @Test
    void isTokenValid_returnsTrueForFreshToken() {
        String token = jwtService.generateAccessToken(testUser);

        assertTrue(jwtService.isTokenValid(token, testUser));
    }

    @Test
    void isTokenValid_returnsFalseForDifferentUser() {
        String token = jwtService.generateAccessToken(testUser);
        UserDetails differentUser = new User("other@example.com", "password", java.util.List.of());

        assertFalse(jwtService.isTokenValid(token, differentUser));
    }

    @Test
    void isTokenValid_throwsForExpiredToken() {
        JwtProperties shortLivedProperties = new JwtProperties();
        shortLivedProperties.setSecret("test-secret-key-for-unit-tests-must-be-long-enough-for-hs512-algorithm-signing");
        shortLivedProperties.setAccessTokenExpiration(-1000L); // already expired

        JwtServiceImpl shortLivedJwtService = new JwtServiceImpl(shortLivedProperties);
        String expiredToken = shortLivedJwtService.generateAccessToken(testUser);

        assertThrows(io.jsonwebtoken.ExpiredJwtException.class,
                () -> shortLivedJwtService.isTokenValid(expiredToken, testUser));
    }

    @Test
    void isTokenValid_throwsForTamperedToken() {
        String token = jwtService.generateAccessToken(testUser);
        String tamperedToken = token.substring(0, token.length() - 5) + "XXXXX";

        assertThrows(io.jsonwebtoken.security.SignatureException.class,
                () -> jwtService.extractUsername(tamperedToken));
    }
}
