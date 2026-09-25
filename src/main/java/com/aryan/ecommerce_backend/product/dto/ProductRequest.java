package com.aryan.ecommerce_backend.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record ProductRequest (
        @NotBlank
        String name,
        @NotNull
        @Positive
        BigDecimal price,
        String description,
        @NotNull @PositiveOrZero
        Integer stockQuantity,
        @NotNull Long categoryId
){}
