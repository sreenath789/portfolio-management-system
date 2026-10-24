package com.sreenath.portfolio.auth.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {

    private String accessToken;
    private String tokenType;
    private String refreshToken;
    private long expiresIn;
}
