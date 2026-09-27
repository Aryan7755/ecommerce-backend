package com.aryan.ecommerce_backend.product.service;

import com.aryan.ecommerce_backend.product.dto.ProductRequest;
import com.aryan.ecommerce_backend.product.dto.ProductResponse;
import com.aryan.ecommerce_backend.product.entity.Category;
import com.aryan.ecommerce_backend.product.entity.Product;
import com.aryan.ecommerce_backend.product.repository.CategoryRepository;
import com.aryan.ecommerce_backend.product.repository.ProductRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductResponse create(ProductRequest productRequest){
        Category category = categoryRepository.findById(productRequest.categoryId())
                .orElseThrow(()-> new IllegalArgumentException("Category Not Found"));

        Product product = Product.builder()
                .name(productRequest.name())
                .description(productRequest.description())
                .price(productRequest.price())
                .stockQuantity(productRequest.stockQuantity())
                .category(category)
                .build();

        productRepository.save(product);
        return toResponse(product);
    }

    public void delete(Long id){
        if(!productRepository.existsById(id)){
            throw new IllegalArgumentException("Product Not Found");
        }
        productRepository.deleteById(id);
    }

    public ProductResponse getById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        return toResponse(product);
    }

    public List<ProductResponse> getAll() {
        return productRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    private ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getCategory().getName(),
                product.getCreatedAt()
        );
    }


    public @Nullable ProductResponse update(Long id, @Valid ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product Not Found"));

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category Not Found"));

        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setStockQuantity(request.stockQuantity());
        product.setCategory(category);

        productRepository.save(product);

        return toResponse(product);
    }
}
