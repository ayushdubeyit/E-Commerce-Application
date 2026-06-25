package com.ayush.ecommerce_app.controller;


import com.ayush.ecommerce_app.dto.category.CategoryResponse;
import com.ayush.ecommerce_app.dto.category.CreateCategoryRequest;
import com.ayush.ecommerce_app.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {

        private final CategoryService categoryService;


        @PostMapping
        public CategoryResponse createCategory(@Valid @RequestBody CreateCategoryRequest request){
            return categoryService.createCategory(request);
        }


        @GetMapping
        public List<CategoryResponse> getAllCategory(){
            return categoryService.getAllCategories();
        }


        @GetMapping("/{id}")
        public CategoryResponse getCategoryById(@PathVariable Long id){
            return categoryService.getCategoryById(id);
        }

        @PutMapping("/{id}")
        public CategoryResponse updateCategory(@PathVariable Long id , @Valid @RequestBody
                                               CreateCategoryRequest request){
            return categoryService.updateCategory(id, request);
        }
       @DeleteMapping("/{id}")
        public void deleteCategory(@PathVariable Long id ){
            categoryService.deleteCategory(id);
        }





}
