package com.aryan.ecommerce_backend.product.service;


import com.aryan.ecommerce_backend.product.dto.CategoryResponse;
import com.aryan.ecommerce_backend.product.entity.Category;
import com.aryan.ecommerce_backend.product.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryResponse create(String name ,Long parentId){
        Category parent =null;
        if (parentId != null) {
            parent = categoryRepository.findById(parentId)
                    .orElseThrow(() -> new IllegalArgumentException("Parent category not found"));
        }
        Category category = Category.builder()
                .name(name)
                .parent(parent)
                .build();
        categoryRepository.save(category);
        return toResponse(category);
    }

    private CategoryResponse toResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getParent() != null ? category.getParent().getId() : null
        );
    }
}
