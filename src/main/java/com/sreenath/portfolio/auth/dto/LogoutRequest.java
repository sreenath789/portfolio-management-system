package com.sreenath.portfolio.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record LogoutRequest(
        @NotBlank(message = "Refresh Token is required")
        String refreshToken
) {
}
