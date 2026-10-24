package com.sreenath.portfolio.portfolio.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdatePortfolioRequest {

    @NotBlank(message = "Portfolio name is required")
    @Size(max = 100, message = "Max length of the portfolioname is 100")
    private String portfolioName;
    @Size(max = 500, message = "Max length of the portfolio description is 500")
    private String description;
}
