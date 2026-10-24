package com.sreenath.portfolio.portfolio.dto;

import com.sreenath.portfolio.holding.dto.HoldingSummaryResponse;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
public class PortfolioSummaryResponse {
    private Long portfolioId;
    private String portfolioName;
    private Integer totalHoldings;
    private Integer totalQuantity;
    private BigDecimal totalInvestedAmount;
    private List<HoldingSummaryResponse> holdingSummaryResponses;
}
