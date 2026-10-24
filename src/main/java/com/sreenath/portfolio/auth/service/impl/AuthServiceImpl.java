package com.sreenath.portfolio.auth.service.impl;

import com.sreenath.portfolio.auth.dto.*;
import com.sreenath.portfolio.auth.entity.RefreshToken;
import com.sreenath.portfolio.auth.entity.Role;
import com.sreenath.portfolio.auth.entity.RoleName;
import com.sreenath.portfolio.auth.entity.User;
import com.sreenath.portfolio.auth.repository.RoleRepository;
import com.sreenath.portfolio.auth.repository.UserRepository;
import com.sreenath.portfolio.auth.security.CustomUserDetails;
import com.sreenath.portfolio.auth.security.jwt.JwtService;
import com.sreenath.portfolio.auth.service.AuthService;
import com.sreenath.portfolio.auth.service.RefreshTokenService;
import com.sreenath.portfolio.common.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @Value(("${jwt.access-token-expiration}"))
    private Long jwtExpiration;
    @Override
    public RegisterResponse register(RegisterRequest registerRequest) {
        if(userRepository.existsByEmail(registerRequest.getEmail())){
            throw new DuplicateResourceException("Email Already Exists, Please Sign In");
        }

        Role investorRole = roleRepository.findByRoleName(RoleName.ROLE_INVESTOR)
                .orElseThrow(() -> new ResourceNotFoundException("Default Role not found"));

        User user = User.builder()
                .email(registerRequest.getEmail())
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .enabled(true)
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .build();

        user.getRoles().add(investorRole);

        User savedUser = userRepository.save(user);

        return RegisterResponse.builder()
                .id(savedUser.getId())
                .email(savedUser.getEmail())
                .fullName(savedUser.getFirstName()+" "+savedUser.getLastName())
                .message("User Registered Successfully")
                .build();
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
       Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        String accessToken = jwtService.generateToken(customUserDetails);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(
                customUserDetails.getUser()
        );

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .tokenType("Bearer")
                .expiresIn(jwtExpiration)
                .build();
    }

    @Override
    public RefreshTokenResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        RefreshToken refreshToken = refreshTokenService.findByToken(refreshTokenRequest.refreshToken());

        refreshTokenService.verifyRefreshToken(refreshToken);

        User user = refreshToken.getUser();

        String accessToken = jwtService.generateToken(user);

        return RefreshTokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .tokenType("Bearer")
                .build();
    }

    @Override
    public LogoutResponse logout(LogoutRequest logoutRequest) {
        refreshTokenService.deleteByToken(logoutRequest.refreshToken());
        return LogoutResponse.builder()
                .message("Logged out Successfully")
                .timeStamp(LocalDateTime.now())
                .build();
    }
}
