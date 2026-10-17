package com.aryan.ecommerce_backend.token.service;

import com.aryan.ecommerce_backend.exception.BadRequestException;
import com.aryan.ecommerce_backend.security.jwt.JwtProperties;
import com.aryan.ecommerce_backend.token.entity.RefreshToken;
import com.aryan.ecommerce_backend.token.repository.RefreshTokenRepository;
import com.aryan.ecommerce_backend.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.HexFormat;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository repository;
    private final JwtProperties jwtProperties;

    static String hash(String token) {
        try {
            byte[] d = MessageDigest.getInstance("SHA-256")
                    .digest(token.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(d);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Transactional
    public void store(User user, String rawToken) {
        repository.save(RefreshToken.builder()
                .user(user)
                .tokenHash(hash(rawToken))
                .expiresAt(LocalDateTime.now()
                        .plusSeconds(jwtProperties.getRefreshTokenExpiration() / 1000))
                .build());
    }

    @Transactional
    public User consume(String rawToken) {
        RefreshToken rt = repository.findByTokenHash(hash(rawToken))
                .orElseThrow(() -> new BadRequestException("Invalid refresh token"));
        if (rt.isRevoked() || rt.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Invalid refresh token");
        }
        rt.setRevoked(true);
        return rt.getUser();
    }

    @Transactional
    public void revoke(String rawToken) {
        repository.findByTokenHash(hash(rawToken)).ifPresent(rt -> rt.setRevoked(true));
    }
}
