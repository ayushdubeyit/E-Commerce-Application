package com.ayush.ecommerce_app.repository;

import com.ayush.ecommerce_app.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {

    boolean existsByName(String name);
}
