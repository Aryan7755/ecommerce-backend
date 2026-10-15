package com.aryan.ecommerce_backend.common;

import com.aryan.ecommerce_backend.cart.dto.*;
import com.aryan.ecommerce_backend.cart.entity.Cart;
import com.aryan.ecommerce_backend.order.dto.*;
import com.aryan.ecommerce_backend.order.entity.Order;
import com.aryan.ecommerce_backend.user.dto.AddressResponse;
import com.aryan.ecommerce_backend.user.entity.Address;
import java.math.BigDecimal;

public final class Mapper {
    private Mapper() {}

    public static AddressResponse toResponse(Address a) {
        return new AddressResponse(a.getId(), a.getStreet(), a.getCity(),
                a.getState(), a.getZip(), a.getIsDefault());
    }

    public static CartResponse toResponse(Cart cart) {
        var items = cart.getItems().stream()
                .map(i -> new CartItemResponse(i.getId(), i.getProduct().getId(),
                        i.getProduct().getName(), i.getProduct().getPrice(), i.getQuantity()))
                .toList();
        BigDecimal total = items.stream()
                .map(i -> i.price().multiply(BigDecimal.valueOf(i.quantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new CartResponse(cart.getId(), items, total);
    }

    public static OrderResponse toResponse(Order o) {
        var items = o.getItems().stream()
                .map(i -> new OrderItemResponse(i.getProduct().getId(), i.getProduct().getName(),
                        i.getQuantity(), i.getPriceAtPurchase()))
                .toList();
        return new OrderResponse(o.getId(), o.getStatus(), o.getTotalAmount(),
                toResponse(o.getAddress()), items, o.getCreatedAt());
    }
}