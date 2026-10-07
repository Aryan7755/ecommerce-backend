package com.aryan.ecommerce_backend.product.controller;

import com.aryan.ecommerce_backend.product.dto.CategoryRequest;
import com.aryan.ecommerce_backend.product.dto.CategoryResponse;
import com.aryan.ecommerce_backend.product.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "Create a new category (admin only)")
    @PostMapping
    public ResponseEntity<CategoryResponse> create(@Valid @RequestBody CategoryRequest request) {

        return ResponseEntity.ok(
                categoryService.create(request)
        );
    }

    @Operation(summary = "Get all product categories")
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAll() {
        return ResponseEntity.ok(categoryService.getAll());
    }

}
