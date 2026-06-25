package com.ayush.ecommerce_app.service;


import com.ayush.ecommerce_app.dto.product.ProductRequest;
import com.ayush.ecommerce_app.dto.product.ProductResponse;

import java.util.List;

public interface ProductService  {

    ProductResponse createProduct(ProductRequest request);

    List<ProductResponse> getAllProducts();

    ProductResponse getProductById(Long id);

    ProductResponse updateProduct(Long id , ProductRequest request);

    void deleteProduct(Long id);
}
