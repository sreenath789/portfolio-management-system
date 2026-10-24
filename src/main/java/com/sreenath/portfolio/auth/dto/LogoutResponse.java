package com.sreenath.portfolio.auth.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record LogoutResponse(
        String message,
        LocalDateTime timeStamp
) {
}
