package com.ayush.ecommerce_app.dto.product;


import lombok.Data;

@Data
public class ProductRequest {

    private String name;
    private String Description;
    private double price;
    private Integer stock;
    private Long CategoryId;

}
