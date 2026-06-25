package com.ayush.ecommerce_app.dto.user;


import com.ayush.ecommerce_app.enums.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private Role role;
}
