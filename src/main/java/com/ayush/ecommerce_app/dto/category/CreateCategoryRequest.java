package com.ayush.ecommerce_app.dto.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateCategoryRequest {


    @NotBlank
    private String name;
}
