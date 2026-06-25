package com.ayush.ecommerce_app.controller;


import com.ayush.ecommerce_app.dto.user.RegisterRequest;
import com.ayush.ecommerce_app.dto.user.UserResponse;
import com.ayush.ecommerce_app.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

        private final UserService userService;

        @PostMapping("/register")
        public UserResponse register(
                @Valid @RequestBody RegisterRequest request) {

            return userService.register(request);
        }

        @GetMapping
        public List<UserResponse> getAllUsers() {

            return userService.getAllUsers();
        }

        @GetMapping("/{id}")
        public UserResponse getUserById(
                @PathVariable Long id) {

            return userService.getUserById(id);
        }
    }
