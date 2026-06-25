package com.ayush.ecommerce_app.dto.category;


import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class CategoryResponse implements Serializable {
     private Long id;
     private String name;

}
