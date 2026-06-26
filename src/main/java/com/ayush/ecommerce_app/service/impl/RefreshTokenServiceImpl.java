package com.ayush.ecommerce_app.service.impl;

import com.ayush.ecommerce_app.entity.RefreshToken;
import com.ayush.ecommerce_app.entity.User;
import com.ayush.ecommerce_app.exception.RefreshTokenExpiredException;
import com.ayush.ecommerce_app.repository.RefreshTokenRepository;
import com.ayush.ecommerce_app.service.RefreshTokenService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {


    private final RefreshTokenRepository repository;
    @Override
    public RefreshToken createRefreshToken(User user) {

        repository.findByUser(user)
                .ifPresent(repository::delete);

        RefreshToken refreshToken = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plus(Duration.ofDays(7)))
                .user(user)
                .build();
        return repository.save(refreshToken);
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if(token.getExpiryDate().isBefore(Instant.now())){
            repository.delete(token);

            throw new RefreshTokenExpiredException(
                    "Refresh Token has Expired Please Login again");
        }
        return token;
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return repository.findByToken(token);
    }

    @Override
    public void deleteByUser(User user) {

        repository.deleteByUser(user);

    }
}
