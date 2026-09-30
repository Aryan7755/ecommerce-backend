package com.aryan.ecommerce_backend.cart.service;

import com.aryan.ecommerce_backend.cart.entity.Cart;
import com.aryan.ecommerce_backend.cart.entity.CartItem;
import com.aryan.ecommerce_backend.cart.repository.CartItemRepository;
import com.aryan.ecommerce_backend.product.entity.Product;
import com.aryan.ecommerce_backend.product.repository.ProductRepository;
import com.aryan.ecommerce_backend.user.entity.User;
import com.aryan.ecommerce_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.aryan.ecommerce_backend.cart.repository.CartRepository;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;


    @Transactional
    public Cart getOrCreateCart(String userEmail){
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return cartRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Cart newCart = Cart.builder().user(user).build();
                    return cartRepository.save(newCart);
                });
    }


    @Transactional
    public Cart addItem(String userEmail, Long productId, Integer quantity) {

        Cart cart = getOrCreateCart(userEmail);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        if (product.getStockQuantity() < quantity) {
            throw new IllegalArgumentException("Insufficient stock");
        }

        var existing = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId);

        if (existing.isPresent()) {
            CartItem item = existing.get();
            int newQuantity = item.getQuantity() + quantity;
            if (product.getStockQuantity() < newQuantity) {
                throw new IllegalArgumentException("Insufficient stock");
            }
            item.setQuantity(newQuantity);
        } else {
            CartItem item = CartItem.builder()
                    .cart(cart).product(product).quantity(quantity).build();
            cart.getItems().add(item);
        }

        return cartRepository.save(cart);

    }


    @Transactional
    public Cart updateQuantity(String userEmail, Long itemId, Integer quantity) {

        Cart cart = getOrCreateCart(userEmail);

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found"));

        if (item.getProduct().getStockQuantity() < quantity) {
            throw new IllegalArgumentException("Insufficient stock");
        }

        item.setQuantity(quantity);
        return cartRepository.save(cart);
    }


    @Transactional
    public void removeItem(String userEmail, Long itemId) {

        Cart cart = getOrCreateCart(userEmail);

        cart.getItems().removeIf(i -> i.getId().equals(itemId));

        cartRepository.save(cart);

    }


    public Cart getCart(String userEmail) {

        return getOrCreateCart(userEmail);

    }


    @Transactional
    public void clearCart(String userEmail) {

        Cart cart = getOrCreateCart(userEmail);

        cart.getItems().clear();

        cartRepository.save(cart);
    }
}


