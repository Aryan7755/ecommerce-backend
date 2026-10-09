package com.aryan.ecommerce_backend.order.dto;

import jakarta.validation.constraints.NotNull;

public record CheckoutRequest(@NotNull Long addressId) {}