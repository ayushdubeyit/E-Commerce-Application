package com.ayush.ecommerce_app.service;

import com.ayush.ecommerce_app.dto.user.RegisterRequest;
import com.ayush.ecommerce_app.dto.user.UserResponse;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;

import java.util.List;

public interface UserService {

    UserResponse register(RegisterRequest request);

    List<UserResponse> getAllUsers();

    UserResponse getUserById(Long id);


}
