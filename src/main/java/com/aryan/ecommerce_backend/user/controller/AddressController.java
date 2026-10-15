package com.aryan.ecommerce_backend.user.controller;

import com.aryan.ecommerce_backend.common.Mapper;
import com.aryan.ecommerce_backend.user.dto.AddressRequest;
import com.aryan.ecommerce_backend.user.dto.AddressResponse;
import com.aryan.ecommerce_backend.user.entity.Address;
import com.aryan.ecommerce_backend.user.service.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/addresses")
public class AddressController {
    private final AddressService addressService;

    @Operation(summary = "Add a new address for the authenticated user")
    @PostMapping
    public ResponseEntity<AddressResponse> create(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody AddressRequest request) {
        Address address = Address.builder()
                .street(request.street())
                .city(request.city())
                .state(request.state())
                .zip(request.zip())
                .isDefault(request.isDefault() != null && request.isDefault())
                .build();
        return ResponseEntity.ok(Mapper.toResponse(
                addressService.create(userDetails.getUsername(), address)));
    }

    @Operation(summary = "Get all addresses for the authenticated user")
    @GetMapping
    public ResponseEntity<List<AddressResponse>> getAll(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(addressService.getAllForUser(userDetails.getUsername())
                .stream().map(Mapper::toResponse).toList());
    }

    @Operation(summary = "Delete an address for the authenticated user")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id) {
        addressService.delete(userDetails.getUsername(), id);
        return ResponseEntity.noContent().build();
    }
}