package com.aryan.ecommerce_backend.order.service;

import com.aryan.ecommerce_backend.order.entity.Order;
import com.aryan.ecommerce_backend.order.entity.OrderStatus;
import com.aryan.ecommerce_backend.order.repository.OrderRepository;
import com.aryan.ecommerce_backend.cart.service.CartService;
import com.aryan.ecommerce_backend.product.repository.ProductRepository;
import com.aryan.ecommerce_backend.user.repository.AddressRepository;
import com.aryan.ecommerce_backend.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock private OrderRepository orderRepository;
    @Mock private CartService cartService;
    @Mock private ProductRepository productRepository;
    @Mock private UserRepository userRepository;
    @Mock private AddressRepository addressRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void updateStatus_validTransition_succeeds() {
        Order order = Order.builder().id(1L).status(OrderStatus.PENDING).build();
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any())).thenReturn(order);

        Order result = orderService.updateStatus(1L, OrderStatus.PAID);

        assertEquals(OrderStatus.PAID, result.getStatus());
    }

    @Test
    void updateStatus_invalidTransition_throwsIllegalStateException() {
        Order order = Order.builder().id(1L).status(OrderStatus.DELIVERED).build();
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertThrows(IllegalStateException.class,
                () -> orderService.updateStatus(1L, OrderStatus.PENDING));
    }

    @Test
    void updateStatus_skipStep_throwsIllegalStateException() {
        Order order = Order.builder().id(1L).status(OrderStatus.PAID).build();
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertThrows(IllegalStateException.class,
                () -> orderService.updateStatus(1L, OrderStatus.DELIVERED));
    }
}