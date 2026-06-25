package com.ayush.ecommerce_app.service.impl;

import com.ayush.ecommerce_app.dto.user.RegisterRequest;
import com.ayush.ecommerce_app.dto.user.UserResponse;
import com.ayush.ecommerce_app.entity.User;
import com.ayush.ecommerce_app.enums.Role;
import com.ayush.ecommerce_app.repository.UserRepository;
import com.ayush.ecommerce_app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.List;
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;
    @Override
    public UserResponse register(RegisterRequest request) {

        if (repository.existsByEmail(request.getEmail())) {
            throw new RuntimeException(
                    "Email already registered");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.CUSTOMER)
                .build();

        User savedUser = repository.save(user);

        return UserResponse.builder()
                .id(savedUser.getId())
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .role(savedUser.getRole())
                .build();
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return repository.findAll()
                .stream()
                .map(User -> UserResponse.builder()
                        .id(User.getId())
                        .name(User.getName())
                        .email(User.getEmail())
                        .role(User.getRole())
                        .build()).toList();
    }

    @Override
    public UserResponse getUserById(Long id) {

        User user = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User Not Found!!!!"));

        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}
