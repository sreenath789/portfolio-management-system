package com.sreenath.portfolio.holding.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateHoldingRequest {
    @NotBlank
    private String stockSymbol;
    @NotNull
    @Positive
    private Integer quantity;
    @NotNull
    @DecimalMin(value="0.01")
    private BigDecimal averageBuyPrice;
}
