package com.sreenath.portfolio.portfolio.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PortfolioResponse {
    private Long id;
    private String portfolioName;
    private String description;
    private LocalDateTime createdAt;
}
