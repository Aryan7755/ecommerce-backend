package com.aryan.ecommerce_backend.user.controller;

import com.aryan.ecommerce_backend.user.entity.Address;
import com.aryan.ecommerce_backend.user.service.AddressService;
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

    @PostMapping
    public ResponseEntity<Address> create(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody Address address) {
        return ResponseEntity.ok(addressService.create(userDetails.getUsername(), address));
    }

    @GetMapping
    public ResponseEntity<List<Address>> getAll(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(addressService.getAllForUser(userDetails.getUsername()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id) {
        addressService.delete(userDetails.getUsername(), id);
        return ResponseEntity.noContent().build();
    }
}
