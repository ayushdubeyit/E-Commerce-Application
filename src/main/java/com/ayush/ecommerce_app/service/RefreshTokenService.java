package com.ayush.ecommerce_app.service;

import com.ayush.ecommerce_app.entity.RefreshToken;
import com.ayush.ecommerce_app.entity.User;

import java.util.Optional;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(User user);
    RefreshToken verifyExpiration(RefreshToken token);
    Optional<RefreshToken> findByToken(String token);
    void deleteByUser(User user);
}
