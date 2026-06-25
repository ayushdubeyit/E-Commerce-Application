package com.ayush.ecommerce_app.service.impl;

import com.ayush.ecommerce_app.dto.product.ProductRequest;
import com.ayush.ecommerce_app.dto.product.ProductResponse;
import com.ayush.ecommerce_app.entity.Category;
import com.ayush.ecommerce_app.entity.Product;
import com.ayush.ecommerce_app.exception.CategoryNotFoundException;
import com.ayush.ecommerce_app.exception.DuplicateResourceException;
import com.ayush.ecommerce_app.exception.ProductNotFoundException;
import com.ayush.ecommerce_app.repository.CategoryRepository;
import com.ayush.ecommerce_app.repository.ProductRepository;
import com.ayush.ecommerce_app.service.ProductService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final StringRedisTemplate redisTemplate;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public ProductResponse createProduct(ProductRequest request) {

        if (productRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("Product already exists");
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new
                        CategoryNotFoundException("Category with id " +
                        request.getCategoryId() + " not found"));

        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .stock(request.getStock())
                .category(category)
                .build();

        Product savedProduct = productRepository.save(product);

        return ProductResponse.builder()
                .id(savedProduct.getId())
                .name(savedProduct.getName())
                .description(savedProduct.getDescription())
                .price(savedProduct.getPrice())
                .stock(savedProduct.getStock())
                .categoryName(savedProduct.getCategory().getName())
                .build();
    }

    @Override
    @Cacheable("allProducts")
    public List<ProductResponse> getAllProducts() {



        return productRepository.findAll()
                .stream()
                .map(product ->
                        ProductResponse.builder()
                                .id(product.getId())
                                .name(product.getName())
                                .description(product.getDescription())
                                .price(product.getPrice())
                                .stock(product.getStock())
                                .categoryName(product.getCategory().getName())
                                .build()
                )
                .toList();
    }

    @Override
    @Cacheable(value = "products", key = "#id")
    public ProductResponse getProductById(Long id) {
//         redisTemplate.opsForValue().set("test" , "Redis working");
//        System.out.println(
//                redisTemplate.opsForValue().get(("test"))
//        );
        System.out.println("DATABASE HIT happen");
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new
                        ProductNotFoundException("Product with id " +
                        id + " not found"));

        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .categoryName(product.getCategory().getName())
                .build();
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "products", key = "#id"),
            @CacheEvict(value = "allProducts", allEntries = true)
    })
    public ProductResponse updateProduct(Long id, ProductRequest request) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("product with id " + id + " not found"));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new
                        CategoryNotFoundException
                        ("Category with id " + request.getCategoryId() + " not found"));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setCategory(category);

        Product updatedProduct = productRepository.save(product);

        return ProductResponse.builder()
                .id(updatedProduct.getId())
                .name(updatedProduct.getName())
                .description(updatedProduct.getDescription())
                .price(updatedProduct.getPrice())
                .stock(updatedProduct.getStock())
                .categoryName(updatedProduct.getCategory().getName())
                .build();
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "products", key = "#id"),
            @CacheEvict(value = "allProducts", allEntries = true)
    })
    public void deleteProduct(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new
                        ProductNotFoundException("Product with id " + id + " not found"));

        productRepository.delete(product);
    }
}