package com.sreenath.portfolio.auth.controller;

import com.sreenath.portfolio.auth.dto.*;
import com.sreenath.portfolio.auth.service.AuthService;
import com.sreenath.portfolio.common.exception.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController //added comments from the master branch
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth") //added comments in the auth controller
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest registerRequest){

        RegisterResponse registerResponse = authService.register(registerRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(registerResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest){
        return ResponseEntity.ok(authService.refreshToken(refreshTokenRequest));
    }

    @PostMapping("/logout")
    public ResponseEntity<LogoutResponse> logout(@Valid @RequestBody LogoutRequest logoutRequest){
        return ResponseEntity.ok(authService.logout(logoutRequest));
    }
}
