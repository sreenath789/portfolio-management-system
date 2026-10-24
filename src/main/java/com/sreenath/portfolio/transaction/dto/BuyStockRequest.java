package com.sreenath.portfolio.transaction.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BuyStockRequest {
    @NotNull
    private Long portfolioId;
    @NotBlank
    private String stockSymbol;
    @NotNull
    @Positive(message = "Quantity should not be negative")
    private Integer quantity;
    @NotNull
    @Positive(message = "Price should not be negative")
    private BigDecimal price;
}
