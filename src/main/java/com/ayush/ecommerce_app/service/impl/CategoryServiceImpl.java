package com.ayush.ecommerce_app.service.impl;

import com.ayush.ecommerce_app.dto.category.CategoryResponse;
import com.ayush.ecommerce_app.dto.category.CreateCategoryRequest;
import com.ayush.ecommerce_app.entity.Category;
import com.ayush.ecommerce_app.exception.CategoryNotFoundException;
import com.ayush.ecommerce_app.exception.DuplicateResourceException;
import com.ayush.ecommerce_app.repository.CategoryRepository;
import com.ayush.ecommerce_app.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;

    @Override
    @CacheEvict(value = "categories", allEntries = true)
    public CategoryResponse createCategory(CreateCategoryRequest request) {
        if (repository.existsByName(request.getName())) {
            throw new DuplicateResourceException(
                    "Category already exists");
        }
        Category category = Category.builder()
                .name(request.getName())
                .build();

        Category savedCategory = repository.save(category);

        return CategoryResponse.builder().id(savedCategory.getId())
                .name(savedCategory.getName())
                .build();


}

    @Override
    @Cacheable("categories")
    public List<CategoryResponse> getAllCategories() {
        return repository.findAll()
                .stream()
                .map(category -> CategoryResponse.builder()
                        .id(category.getId())
                        .name(category.getName())
                        .build()
                ).toList();

    }

    @Override
    @Cacheable(value = "categories", key = "#id")
    public CategoryResponse getCategoryById(Long id) {
        Category category = repository.findById(id)
                .orElseThrow(() ->
                        new CategoryNotFoundException("Category with id " + id + " not found"));

        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "categories", key = "#id"),
            @CacheEvict(value = "categories", allEntries = true)
    })
    public CategoryResponse updateCategory(Long id, CreateCategoryRequest request) {

        Category category = repository.findById(id)
                .orElseThrow(() ->
                        new CategoryNotFoundException("Category with id  " +  id + " not found"));

        category.setName(request.getName());

        Category updatedCategory =
                repository.save(category);

        return CategoryResponse.builder()
                .id(updatedCategory.getId())
                .name(updatedCategory.getName())
                .build();
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "categories", key = "#id"),
            @CacheEvict(value = "categories", allEntries = true)
    })
    public void deleteCategory(Long id) {

        Category category = repository.findById(id)
                .orElseThrow(() ->
                        new CategoryNotFoundException("Category with id " + id + " not found"));

        repository.delete(category);
    }
}
