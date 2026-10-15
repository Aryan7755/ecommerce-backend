package com.aryan.ecommerce_backend.cart.dto;

import java.math.BigDecimal;

public record CartItemResponse(Long id, Long productId, String productName,
                               BigDecimal price, Integer quantity) {}