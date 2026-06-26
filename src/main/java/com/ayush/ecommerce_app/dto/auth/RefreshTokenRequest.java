package com.ayush.ecommerce_app.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
public class RefreshTokenRequest {


    @NotBlank(message = "Refresh Token is Required")
    private String refreshToken;

}
