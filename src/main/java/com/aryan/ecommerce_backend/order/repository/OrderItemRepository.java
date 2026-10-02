package com.aryan.ecommerce_backend.order.repository;
import com.aryan.ecommerce_backend.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}