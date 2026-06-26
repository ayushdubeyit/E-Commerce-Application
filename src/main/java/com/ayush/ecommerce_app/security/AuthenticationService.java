package com.ayush.ecommerce_app.security;


import com.ayush.ecommerce_app.dto.auth.RefreshTokenRequest;
import com.ayush.ecommerce_app.dto.auth.RefreshTokenResponse;
import com.ayush.ecommerce_app.dto.login.AuthResponse;
import com.ayush.ecommerce_app.dto.login.LoginRequest;
import com.ayush.ecommerce_app.entity.RefreshToken;
import com.ayush.ecommerce_app.entity.User;
import com.ayush.ecommerce_app.repository.UserRepository;
import com.ayush.ecommerce_app.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;

    public AuthResponse login(LoginRequest request){
         authenticationManager.authenticate(
                 new UsernamePasswordAuthenticationToken(
                         request.getEmail(),
                         request.getPassword()
                 )
         );
         User user = userRepository.findByEmail(request.getEmail())
                 .orElseThrow(()-> new RuntimeException("User Not Found"));
         String accessToken = jwtService.generateToken(request.getEmail());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);


        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .build();
    }

    public RefreshTokenResponse refreshToken(RefreshTokenRequest request){
        RefreshToken refreshToken = refreshTokenService.findByToken(request.getRefreshToken())
                .orElseThrow(()-> new RuntimeException("Refresh Token not found"));

        refreshTokenService.verifyExpiration(refreshToken);

        String accessToken = jwtService.generateToken(refreshToken.getUser().getEmail());

        return RefreshTokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .build();
    }


}
