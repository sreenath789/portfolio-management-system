package com.sreenath.portfolio.auth.service;

import com.sreenath.portfolio.auth.dto.*;

public interface AuthService {

    RegisterResponse register(RegisterRequest registerRequest);
    LoginResponse login(LoginRequest loginRequest);

    RefreshTokenResponse refreshToken(RefreshTokenRequest refreshTokenRequest);

    LogoutResponse logout(LogoutRequest logoutRequest);
}
