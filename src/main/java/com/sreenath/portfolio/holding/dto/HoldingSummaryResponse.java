package com.sreenath.portfolio.holding.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class HoldingSummaryResponse {
    private String stockSymbol;
    private Integer quantity;
    private BigDecimal averageBuyPrice;
    private BigDecimal totalInvestedValue;
}
