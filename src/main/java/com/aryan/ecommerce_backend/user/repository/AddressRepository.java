package com.aryan.ecommerce_backend.user.repository;

import com.aryan.ecommerce_backend.user.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address,Long> {
    List<Address> findByUserId(Long userId);
}
