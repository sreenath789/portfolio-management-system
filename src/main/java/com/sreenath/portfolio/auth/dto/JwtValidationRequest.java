package com.sreenath.portfolio.auth.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtValidationRequest {
    private String token;
}
