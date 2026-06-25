package com.ayush.ecommerce_app.entity;

import com.ayush.ecommerce_app.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;


    @NotNull
    private String password;


    @Enumerated(EnumType.STRING)
    private Role role;
}