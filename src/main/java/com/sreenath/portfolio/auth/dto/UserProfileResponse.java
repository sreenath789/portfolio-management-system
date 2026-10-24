package com.sreenath.portfolio.auth.dto;

import lombok.Builder;

import java.util.Set;

@Builder
public record UserProfileResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        Set<String> roles
) { }
