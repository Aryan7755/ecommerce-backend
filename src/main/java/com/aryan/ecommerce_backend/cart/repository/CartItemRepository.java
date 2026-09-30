package com.aryan.ecommerce_backend.cart.repository;

import com.aryan.ecommerce_backend.cart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    Optional<CartItem> findByCartIdAndProductId(Long cartId, Long productId);
}
