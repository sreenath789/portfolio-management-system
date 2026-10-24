package com.sreenath.portfolio.auth.dto;

import lombok.Builder;

@Builder
public record RefreshTokenResponse(
        String accessToken,
        String refreshToken,
        String tokenType
) {
}
