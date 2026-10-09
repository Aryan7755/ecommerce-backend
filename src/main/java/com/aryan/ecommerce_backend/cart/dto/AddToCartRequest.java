package com.aryan.ecommerce_backend.cart.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AddToCartRequest(@NotNull Long productId, @NotNull @Positive Integer quantity) {}