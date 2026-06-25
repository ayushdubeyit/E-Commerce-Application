package com.ayush.ecommerce_app.dto.user;

import lombok.Data;

@Data
public class RegisterRequest {

    private String name;
    private String email;
    private String password;
}
