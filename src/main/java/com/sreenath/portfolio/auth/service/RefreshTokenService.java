package com.sreenath.portfolio.auth.service;

import com.sreenath.portfolio.auth.entity.User;
import com.sreenath.portfolio.auth.entity.RefreshToken;
import com.sreenath.portfolio.auth.repository.RefreshTokenRepository;
import com.sreenath.portfolio.auth.repository.UserRepository;
import com.sreenath.portfolio.common.exception.ResourceNotFoundException;
import com.sreenath.portfolio.common.exception.TokenExpiredException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Value("${jwt.refresh-token-expiration}")
    private Long refreshTokenExpiration;

    public RefreshToken createRefreshToken(User user){
        RefreshToken refreshToken = RefreshToken
                .builder()
                .token(UUID.randomUUID().toString())
                .expiryDate(LocalDateTime.now().plusSeconds(refreshTokenExpiration/1000))
                .user(user)
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    public void verifyRefreshToken(RefreshToken refreshToken){
        if(refreshToken.getExpiryDate().isBefore(LocalDateTime.now())){
            refreshTokenRepository.delete(refreshToken);

            throw new TokenExpiredException("Refresh Token is expired! Please Login");
        }
    }

    public RefreshToken findByToken(String token){
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(()->new ResourceNotFoundException("Refresh Token not found"));
    }

    @Transactional
    public void deleteByToken(String token){
        refreshTokenRepository.deleteByToken(token);
    }
}
