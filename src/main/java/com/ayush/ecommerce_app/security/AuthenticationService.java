package com.ayush.ecommerce_app.security;


import com.ayush.ecommerce_app.dto.login.AuthResponse;
import com.ayush.ecommerce_app.dto.login.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthResponse login(LoginRequest request){
         authenticationManager.authenticate(
                 new UsernamePasswordAuthenticationToken(
                         request.getEmail(),
                         request.getPassword()
                 )
         );


        String token =
                jwtService.generateToken(
                        request.getEmail()
                );
        return AuthResponse.builder().token(token).build();
    }


}
