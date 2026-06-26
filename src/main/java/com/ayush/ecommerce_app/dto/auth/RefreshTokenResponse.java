package com.ayush.ecommerce_app.dto.auth;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@AllArgsConstructor
@Data
@Builder
public class RefreshTokenResponse {

    private String refreshToken;
    private String accessToken;

}
