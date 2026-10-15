package com.aryan.ecommerce_backend.user.dto;

public record AddressResponse(Long id, String street, String city,
                              String state, String zip, Boolean isDefault) {}