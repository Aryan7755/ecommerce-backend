package com.aryan.ecommerce_backend.order.controller;

import com.aryan.ecommerce_backend.AbstractIntegrationTest;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class CheckoutIntegrationTest extends AbstractIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @Autowired
    private com.aryan.ecommerce_backend.product.repository.CategoryRepository categoryRepository;

    @Autowired
    private com.aryan.ecommerce_backend.product.repository.ProductRepository productRepository;

    @Test
    void fullCheckoutFlow_registerToOrder() throws Exception {
        // 1. Register
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Buyer\",\"email\":\"buyer@test.com\",\"password\":\"password123\"}"));

        // 2. Login
        var loginResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"buyer@test.com\",\"password\":\"password123\"}"))
                .andReturn();
        String token = objectMapper.readTree(loginResult.getResponse().getContentAsString())
                .get("accessToken").asText();

        var category = categoryRepository.save(
                com.aryan.ecommerce_backend.product.entity.Category.builder()
                        .name("Test Category")
                        .build());

        productRepository.save(
                com.aryan.ecommerce_backend.product.entity.Product.builder()
                        .name("Test Product")
                        .price(new java.math.BigDecimal("10.00"))
                        .stockQuantity(100)
                        .category(category)
                        .build());

        // 3. Add address
        var addressResult = mockMvc.perform(post("/api/addresses")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"street\":\"1 Test St\",\"city\":\"TestCity\",\"state\":\"TS\",\"zip\":\"00000\",\"isDefault\":true}"))
                .andExpect(status().isOk())
                .andReturn();
        long addressId = objectMapper.readTree(addressResult.getResponse().getContentAsString())
                .get("id").asLong();

        // 4. Add to cart (assumes product id 1 exists)
        mockMvc.perform(post("/api/cart/items")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productId\":1,\"quantity\":1}"))
                .andExpect(status().isOk());

        // 5. Checkout
        mockMvc.perform(post("/api/orders/checkout")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("{\"addressId\":%d}", addressId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PENDING"));
    }
}