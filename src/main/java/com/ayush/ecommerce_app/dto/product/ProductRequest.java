package com.ayush.ecommerce_app.dto.product;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class ProductRequest {


    @NotBlank(message = "Product name is required")
    private String name;


    private String description;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than zero")
    private Double price;

    @NotNull
    @PositiveOrZero
    private Integer stock;

    @NotNull(message = "Category is required")
    private Long categoryId;


}
