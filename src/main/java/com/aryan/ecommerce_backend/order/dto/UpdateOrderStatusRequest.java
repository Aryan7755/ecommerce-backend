package com.aryan.ecommerce_backend.order.dto;

import com.aryan.ecommerce_backend.order.entity.OrderStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateOrderStatusRequest(@NotNull OrderStatus status) {}