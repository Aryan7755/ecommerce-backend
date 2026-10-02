package com.aryan.ecommerce_backend.product.service;

import com.aryan.ecommerce_backend.exception.ResourceNotFoundException;
import com.aryan.ecommerce_backend.product.dto.CategoryRequest;
import com.aryan.ecommerce_backend.product.dto.CategoryResponse;
import com.aryan.ecommerce_backend.product.entity.Category;
import com.aryan.ecommerce_backend.product.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryResponse create(CategoryRequest request) {

        Category parent = null;

        if (request.parentId() != null) {
            parent = categoryRepository.findById(request.parentId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Parent category not found"));
        }

        Category category = Category.builder()
                .name(request.name())
                .parent(parent)
                .build();

        categoryRepository.save(category);

        return toResponse(category);
    }

    private CategoryResponse toResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getParent() != null
                        ? category.getParent().getId()
                        : null
        );
    }

    public List<CategoryResponse> getAll() {
        return categoryRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }
}