package com.aryan.ecommerce_backend.cart.controller;

import com.aryan.ecommerce_backend.cart.dto.AddToCartRequest;
import com.aryan.ecommerce_backend.cart.dto.CartResponse;
import com.aryan.ecommerce_backend.cart.dto.UpdateCartItemRequest;
import com.aryan.ecommerce_backend.cart.entity.Cart;
import com.aryan.ecommerce_backend.cart.service.CartService;
import com.aryan.ecommerce_backend.common.Mapper;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    @Operation(summary = "Get the authenticated user's cart")
    @GetMapping
    public ResponseEntity<CartResponse> getCart(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(Mapper.toResponse(cartService.getCart(userDetails.getUsername())));
    }

    @Operation(summary = "Add a product to the cart")
    @PostMapping("/items")
    public ResponseEntity<CartResponse> addItem(@AuthenticationPrincipal UserDetails userDetails,
                                        @Valid @RequestBody AddToCartRequest request) {
        return ResponseEntity.ok(Mapper.toResponse(cartService.addItem(
                userDetails.getUsername(), request.productId(), request.quantity())));
    }

    @Operation(summary = "Update the quantity of a cart item")
    @PutMapping("/items/{id}")
    public ResponseEntity<CartResponse> updateQuantity(@AuthenticationPrincipal UserDetails userDetails,
                                               @PathVariable Long id,
                                               @Valid @RequestBody UpdateCartItemRequest request) {
        return ResponseEntity.ok(Mapper.toResponse(cartService.updateQuantity(
                userDetails.getUsername(), id, request.quantity())));
    }

    @Operation(summary = "Remove an item from the cart")
    @DeleteMapping("/items/{id}")
    public ResponseEntity<Void> removeItem(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {
        cartService.removeItem(userDetails.getUsername(), id);
        return ResponseEntity.noContent().build();
    }
}
