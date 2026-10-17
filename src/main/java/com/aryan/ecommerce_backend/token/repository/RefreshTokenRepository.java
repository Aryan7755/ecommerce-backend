package com.aryan.ecommerce_backend.token.repository;

import com.aryan.ecommerce_backend.token.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
    Optional<RefreshToken> findByTokenHash(String tokenHash);
}
