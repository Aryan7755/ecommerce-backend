package com.aryan.ecommerce_backend.auth.dto;

public record AuthResponse(
        String accessToken,
        String refreshToken
) {}
