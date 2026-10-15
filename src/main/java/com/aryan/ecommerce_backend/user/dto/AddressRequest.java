package com.aryan.ecommerce_backend.user.dto;

import jakarta.validation.constraints.NotBlank;

public record AddressRequest(@NotBlank String street, @NotBlank String city,
                             @NotBlank String state, @NotBlank String zip,
                             Boolean isDefault) {}