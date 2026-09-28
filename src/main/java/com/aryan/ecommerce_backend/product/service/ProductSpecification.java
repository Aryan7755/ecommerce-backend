package com.aryan.ecommerce_backend.product.service;


import com.aryan.ecommerce_backend.product.entity.Product;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class ProductSpecification {

    public static Specification<Product> hasCategoryId(Long id){
        return (root, query, criteriaBuilder) ->
            id==null ? null : criteriaBuilder.equal(root.get("category").get("id"), id);
    }

    public static Specification<Product> priceGreaterThanOrEqual(BigDecimal min) {
        return (root, query, cb) ->
                min == null ? null : cb.greaterThanOrEqualTo(root.get("price"), min);
    }

    public static Specification<Product> priceLessThanOrEqual(BigDecimal max) {
        return (root, query, cb) ->
                max == null ? null : cb.lessThanOrEqualTo(root.get("price"), max);
    }
}
