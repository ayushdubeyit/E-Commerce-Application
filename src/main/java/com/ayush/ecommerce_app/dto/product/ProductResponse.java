package com.ayush.ecommerce_app.dto.product;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse implements Serializable {

    private Long id;

    private String name;

    private String description;

    private Double price;

    private Integer stock;

    private String categoryName;
}
