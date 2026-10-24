package com.sreenath.portfolio.holding.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HoldingResponse {

    private Long id;
    private String stockSymbol;
    private Integer quantity;
    private BigDecimal averageBuyPrice;
    private LocalDateTime createdAt;

}
