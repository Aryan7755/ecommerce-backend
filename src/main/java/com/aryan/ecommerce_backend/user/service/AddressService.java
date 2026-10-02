package com.aryan.ecommerce_backend.user.service;


import com.aryan.ecommerce_backend.exception.ResourceNotFoundException;
import com.aryan.ecommerce_backend.user.entity.Address;
import com.aryan.ecommerce_backend.user.entity.User;
import com.aryan.ecommerce_backend.user.repository.AddressRepository;
import com.aryan.ecommerce_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public Address create(String userEmail, Address address) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        address.setUser(user);
        return addressRepository.save(address);
    }

    public List<Address> getAllForUser(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return addressRepository.findByUserId(user.getId());
    }

    public void delete(String userEmail, Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        if (!address.getUser().getEmail().equals(userEmail)) {
            throw new SecurityException("Cannot delete another user's address");
        }
        addressRepository.deleteById(addressId);
    }
}