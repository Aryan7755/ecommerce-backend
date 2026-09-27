package com.aryan.ecommerce_backend.product.dto;

public record CategoryResponse(
        Long id,
        String name,
        Long parentId
) {}
