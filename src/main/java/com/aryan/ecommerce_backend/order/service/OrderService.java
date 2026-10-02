package com.aryan.ecommerce_backend.order.service;

import com.aryan.ecommerce_backend.cart.entity.Cart;
import com.aryan.ecommerce_backend.cart.entity.CartItem;
import com.aryan.ecommerce_backend.cart.service.CartService;
import com.aryan.ecommerce_backend.order.entity.Order;
import com.aryan.ecommerce_backend.order.entity.OrderItem;
import com.aryan.ecommerce_backend.order.entity.OrderStatus;
import com.aryan.ecommerce_backend.order.repository.OrderRepository;
import com.aryan.ecommerce_backend.product.entity.Product;
import com.aryan.ecommerce_backend.product.repository.ProductRepository;
import com.aryan.ecommerce_backend.user.entity.Address;
import com.aryan.ecommerce_backend.user.entity.User;
import com.aryan.ecommerce_backend.user.repository.AddressRepository;
import com.aryan.ecommerce_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    @Transactional
    public Order checkout(String userEmail, Long addressId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new IllegalArgumentException("Address not found"));

        Cart cart = cartService.getCart(userEmail);
        if (cart.getItems().isEmpty()) {
            throw new IllegalStateException("Cart is empty");
        }

        // Re-validate stock at checkout time — items in cart may have been
        // added minutes/hours ago; another user could have bought the
        // last units in the meantime. This is why we check again here,
        // not just trust the cart's earlier validation.
        for (CartItem cartItem : cart.getItems()) {
            Product product = cartItem.getProduct();
            if (product.getStockQuantity() < cartItem.getQuantity()) {
                throw new IllegalStateException(
                        "Insufficient stock for product: " + product.getName());
            }
        }

        Order order = Order.builder()
                .user(user)
                .address(address)
                .status(OrderStatus.PENDING)
                .build();

        BigDecimal total = BigDecimal.ZERO;

        for (CartItem cartItem : cart.getItems()) {
            Product product = cartItem.getProduct();

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(cartItem.getQuantity())
                    .priceAtPurchase(product.getPrice())  // SNAPSHOT taken here
                    .build();

            order.getItems().add(orderItem);

            total = total.add(product.getPrice()
                    .multiply(BigDecimal.valueOf(cartItem.getQuantity())));

            // Decrement stock now, inside the same transaction
            product.setStockQuantity(product.getStockQuantity() - cartItem.getQuantity());
            productRepository.save(product);
        }

        order.setTotalAmount(total);
        orderRepository.save(order);

        cartService.clearCart(userEmail);

        return order;
    }
}