package com.aryan.ecommerce_backend.product.controller;

import com.aryan.ecommerce_backend.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProductControllerIntegrationTest extends AbstractIntegrationTest {

    @Autowired private MockMvc mockMvc;

    private String registerAndLogin(String email, String role) throws Exception {
       String registerBody = String.format(
                "{\"name\":\"Test\",\"email\":\"%s\",\"password\":\"password123\"}", email);

       mockMvc.perform(
               post("/api/auth/register")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(registerBody)).andExpect(status().isOk());

       var loginResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format(
                                "{\"email\":\"%s\",\"password\":\"password123\"}", email)))
                .andReturn();

       return loginResult.getResponse().getContentAsString();
    }

    @Test
    void regularUser_cannotCreateProduct_returns403() throws Exception {
        String response = registerAndLogin("user@test.com", "USER");
        String token = com.fasterxml.jackson.databind.json.JsonMapper.builder().build()
                .readTree(response).get("accessToken").asText();

        String productBody = """
                {"name":"Test Product","price":10.0,"stockQuantity":5,"categoryId":1}
                """;

        mockMvc.perform(post("/api/products")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productBody))
                .andExpect(status().isForbidden());
    }

    @Test
    void publicGet_doesNotRequireAuth() throws Exception {
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk());
    }


}
