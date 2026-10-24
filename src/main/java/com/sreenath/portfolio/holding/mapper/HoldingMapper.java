package com.sreenath.portfolio.holding.mapper;

import com.sreenath.portfolio.holding.dto.CreateHoldingRequest;
import com.sreenath.portfolio.holding.dto.HoldingResponse;
import com.sreenath.portfolio.holding.entity.Holding;
import com.sreenath.portfolio.portfolio.entity.Portfolio;
import org.springframework.stereotype.Component;

@Component
public class HoldingMapper {

    public Holding toEntity(CreateHoldingRequest createHoldingRequest, Portfolio portfolio,String stockSymbol){
        return Holding.builder()
                .stockSymbol(stockSymbol)
                .quantity(createHoldingRequest.getQuantity())
                .avgBuyPrice(createHoldingRequest.getAverageBuyPrice())
                .portfolio(portfolio)
                .build();
    }

    public HoldingResponse toResponse(Holding holding){
        return HoldingResponse.builder()
                .id(holding.getId())
                .stockSymbol(holding.getStockSymbol())
                .quantity(holding.getQuantity())
                .averageBuyPrice(holding.getAvgBuyPrice())
                .createdAt(holding.getCreatedAt())
                .build();
    }
}
