package com.aryan.ecommerce_backend.cart.controller;

import com.aryan.ecommerce_backend.cart.entity.Cart;
import com.aryan.ecommerce_backend.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<Cart> getCart(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(cartService.getCart(userDetails.getUsername()));
    }

    @PostMapping("/items")
    public ResponseEntity<Cart> addItem(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody Map<String, Object> body) {
        Long productId = Long.valueOf(body.get("productId").toString());
        Integer quantity = Integer.valueOf(body.get("quantity").toString());
        return ResponseEntity.ok(cartService.addItem(userDetails.getUsername(), productId, quantity));
    }

    @PutMapping("/items/{id}")
    public ResponseEntity<Cart> updateQuantity(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id,
            @RequestBody Map<String, Object> body) {
        Integer quantity = Integer.valueOf(body.get("quantity").toString());
        return ResponseEntity.ok(cartService.updateQuantity(userDetails.getUsername(), id, quantity));
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<Void> removeItem(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {
        cartService.removeItem(userDetails.getUsername(), id);
        return ResponseEntity.noContent().build();
    }
}
