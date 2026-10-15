package com.aryan.ecommerce_backend.order.dto;

import com.aryan.ecommerce_backend.order.entity.OrderStatus;
import com.aryan.ecommerce_backend.user.dto.AddressResponse;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(Long id, OrderStatus status, BigDecimal totalAmount,
                            AddressResponse address, List<OrderItemResponse> items,
                            LocalDateTime createdAt) {}