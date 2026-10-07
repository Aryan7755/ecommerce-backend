package com.aryan.ecommerce_backend.order.controller;

import com.aryan.ecommerce_backend.order.entity.Order;
import com.aryan.ecommerce_backend.order.entity.OrderStatus;
import com.aryan.ecommerce_backend.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
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
    public ResponseEntity<Order> checkout(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody Map<String, Object> body) {
        Long addressId = Long.valueOf(body.get("addressId").toString());
        return ResponseEntity.ok(orderService.checkout(userDetails.getUsername(), addressId));
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
    public ResponseEntity<Order> updateStatus(
            @PathVariable Long id, @RequestBody Map<String, String> body) {
        OrderStatus newStatus = OrderStatus.valueOf(body.get("status"));
        return ResponseEntity.ok(orderService.updateStatus(id, newStatus));
    }
}
