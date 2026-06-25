package com.ayush.ecommerce_app.service;

import com.ayush.ecommerce_app.dto.category.CategoryResponse;
import com.ayush.ecommerce_app.dto.category.CreateCategoryRequest;

import java.util.List;

public interface CategoryService {


    CategoryResponse createCategory(CreateCategoryRequest request);

    List<CategoryResponse> getAllCategories();

    CategoryResponse getCategoryById(Long id);

    CategoryResponse updateCategory(Long id , CreateCategoryRequest request);

    void deleteCategory(Long id);



}
