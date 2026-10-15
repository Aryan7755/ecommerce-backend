package com.aryan.ecommerce_backend.order.controller;

import com.aryan.ecommerce_backend.common.Mapper;
import com.aryan.ecommerce_backend.order.dto.CheckoutRequest;
import com.aryan.ecommerce_backend.order.dto.OrderResponse;
import com.aryan.ecommerce_backend.order.dto.UpdateOrderStatusRequest;
import com.aryan.ecommerce_backend.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "Create an order from the user's cart")
    @PostMapping("/checkout")
    public ResponseEntity<OrderResponse> checkout(@AuthenticationPrincipal UserDetails userDetails,
                                                  @Valid @RequestBody CheckoutRequest request) {
        return ResponseEntity.ok(Mapper.toResponse(
                orderService.checkout(userDetails.getUsername(), request.addressId())));
    }

    @Operation(summary = "Get all orders for the authenticated user")
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getMyOrders(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(orderService.getOrdersForUser(userDetails.getUsername())
                .stream().map(Mapper::toResponse).toList());
    }

    @Operation(summary = "Get a specific order for the authenticated user")
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getById(
            @AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id) {
        return ResponseEntity.ok(Mapper.toResponse(
                orderService.getById(userDetails.getUsername(), id)));
    }

    @Operation(summary = "Update an order's status (admin only)")
    @PutMapping("/{id}/status")
    public ResponseEntity<OrderResponse> updateStatus(@PathVariable Long id,
                                                      @Valid @RequestBody UpdateOrderStatusRequest request) {
        return ResponseEntity.ok(Mapper.toResponse(
                orderService.updateStatus(id, request.status())));
    }
}