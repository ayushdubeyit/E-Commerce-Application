package com.ayush.ecommerce_app.controller;


import com.ayush.ecommerce_app.dto.auth.RefreshTokenRequest;
import com.ayush.ecommerce_app.dto.auth.RefreshTokenResponse;
import com.ayush.ecommerce_app.dto.login.AuthResponse;
import com.ayush.ecommerce_app.dto.login.LoginRequest;
import com.ayush.ecommerce_app.dto.user.RegisterRequest;
import com.ayush.ecommerce_app.dto.user.UserResponse;
import com.ayush.ecommerce_app.security.AuthenticationService;
import com.ayush.ecommerce_app.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
    @RequestMapping("/auth")
    @RequiredArgsConstructor
    public class AuthController {

        private final AuthenticationService authenticationService;

        private final UserService userService;

        @PostMapping("/register")
        public UserResponse register(
               @Valid @RequestBody RegisterRequest request) {

            return userService.register(request);
        }

        @PostMapping("/login")
        public AuthResponse login(
              @Valid  @RequestBody LoginRequest request) {

            return authenticationService.login(request);
        }

    @PostMapping("/refresh-token")
    public RefreshTokenResponse refreshToken(
            @Valid @RequestBody RefreshTokenRequest request){

        return authenticationService.refreshToken(request);
    }
    }

