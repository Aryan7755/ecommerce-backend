package com.aryan.ecommerce_backend.order.controller;

import com.aryan.ecommerce_backend.order.dto.CheckoutRequest;
import com.aryan.ecommerce_backend.order.dto.UpdateOrderStatusRequest;
import com.aryan.ecommerce_backend.order.entity.Order;
import com.aryan.ecommerce_backend.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "Create an order from the user's cart")
    @PostMapping("/checkout")
    public ResponseEntity<Order> checkout(@AuthenticationPrincipal UserDetails userDetails,
                                          @Valid @RequestBody CheckoutRequest request) {
        return ResponseEntity.ok(orderService.checkout(userDetails.getUsername(), request.addressId()));
    }

    @Operation(summary = "Get all orders for the authenticated user")
    @GetMapping
    public ResponseEntity<List<Order>> getMyOrders(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(orderService.getOrdersForUser(userDetails.getUsername()));
    }

    @Operation(summary = "Get a specific order for the authenticated user")
    @GetMapping("/{id}")
    public ResponseEntity<Order> getById(
            @AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id) {
        return ResponseEntity.ok(orderService.getById(userDetails.getUsername(), id));
    }

    @Operation(summary = "Update an order's status (admin only)")
    @PutMapping("/{id}/status")
    public ResponseEntity<Order> updateStatus(@PathVariable Long id,
                                              @Valid @RequestBody UpdateOrderStatusRequest request) {
        return ResponseEntity.ok(orderService.updateStatus(id, request.status()));
    }
}
