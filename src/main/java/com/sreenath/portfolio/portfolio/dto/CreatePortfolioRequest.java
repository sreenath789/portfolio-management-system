package com.sreenath.portfolio.portfolio.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatePortfolioRequest {
    @NotBlank(message = "Portfolio name is required")
    @Size(max = 100,message = "Portfolio name max length is 100")
    private String portfolioName;
    @Size(max = 500,message = "Portfolio description max length is 500")
    private String description;
}
