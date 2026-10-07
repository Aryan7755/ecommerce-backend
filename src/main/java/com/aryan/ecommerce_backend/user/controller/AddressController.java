package com.aryan.ecommerce_backend.user.controller;

import com.aryan.ecommerce_backend.user.entity.Address;
import com.aryan.ecommerce_backend.user.service.AddressService;
import io.swagger.v3.oas.annotations.Operation;
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
    public ResponseEntity<Address> create(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody Address address) {
        return ResponseEntity.ok(addressService.create(userDetails.getUsername(), address));
    }

    @Operation(summary = "Get all addresses for the authenticated user")
    @GetMapping
    public ResponseEntity<List<Address>> getAll(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(addressService.getAllForUser(userDetails.getUsername()));
    }

    @Operation(summary = "Delete an address for the authenticated user")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id) {
        addressService.delete(userDetails.getUsername(), id);
        return ResponseEntity.noContent().build();
    }
}
