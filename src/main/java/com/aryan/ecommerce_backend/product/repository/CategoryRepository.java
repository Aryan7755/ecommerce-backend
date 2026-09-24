package com.aryan.ecommerce_backend.product.repository;

import com.aryan.ecommerce_backend.product.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {
}
