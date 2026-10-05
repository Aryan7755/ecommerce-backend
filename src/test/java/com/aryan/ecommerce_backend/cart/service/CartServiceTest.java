package com.aryan.ecommerce_backend.cart.service;

import com.aryan.ecommerce_backend.cart.entity.Cart;
import com.aryan.ecommerce_backend.cart.repository.CartItemRepository;
import com.aryan.ecommerce_backend.cart.repository.CartRepository;
import com.aryan.ecommerce_backend.product.entity.Product;
import com.aryan.ecommerce_backend.product.repository.ProductRepository;
import com.aryan.ecommerce_backend.user.entity.User;
import com.aryan.ecommerce_backend.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock private CartRepository cartRepository;
    @Mock private CartItemRepository cartItemRepository;
    @Mock private ProductRepository productRepository;
    @Mock private UserRepository userRepository;

    @InjectMocks
    private CartService cartService;

    @Test
    void addItem_insufficientStock_throwsIllegalStateException() {
        User user = User.builder().id(1L).email("test@example.com").build();
        Product product = Product.builder()
                .id(1L).name("Laptop").price(BigDecimal.valueOf(999.99))
                .stockQuantity(2).build();
        Cart cart = Cart.builder().id(1L).user(user).build();

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        assertThrows(IllegalStateException.class,
                () -> cartService.addItem("test@example.com", 1L, 5));
    }

    @Test
    void addItem_sufficientStock_succeeds() {
        User user = User.builder().id(1L).email("test@example.com").build();
        Product product = Product.builder()
                .id(1L).name("Laptop").price(BigDecimal.valueOf(999.99))
                .stockQuantity(10).build();
        Cart cart = Cart.builder().id(1L).user(user).build();

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(cartItemRepository.findByCartIdAndProductId(1L, 1L)).thenReturn(Optional.empty());
        when(cartRepository.save(any())).thenReturn(cart);

        assertDoesNotThrow(() -> cartService.addItem("test@example.com", 1L, 3));
    }
}