package com.aryan.ecommerce_backend.order.repository;
import com.aryan.ecommerce_backend.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
}