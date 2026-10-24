package com.sreenath.portfolio.holding.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateHoldingRequest {
    @NotBlank(message = "Stock Symbol is required")
    @Size(max = 20)
    private String stockSymbol;
    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity should be greater than zero")
    private Integer quantity;
    @NotNull(message = "Average buy price is required")
    @DecimalMin(value="0.01",message = "Average buy price should be greater than 0")
    private BigDecimal averageBuyPrice;
}
