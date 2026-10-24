package com.sreenath.portfolio.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequest {

    @Email
    @NotBlank(message = "Email is required")
    private String email;
    @NotBlank(message="Password is required")
    private String password;
}
