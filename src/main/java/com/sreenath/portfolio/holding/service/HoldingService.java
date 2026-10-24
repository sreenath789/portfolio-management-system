package com.sreenath.portfolio.holding.service;

import com.sreenath.portfolio.auth.entity.User;
import com.sreenath.portfolio.holding.dto.CreateHoldingRequest;
import com.sreenath.portfolio.holding.dto.HoldingResponse;
import com.sreenath.portfolio.holding.dto.UpdateHoldingRequest;

import java.util.List;

public interface HoldingService {
    HoldingResponse createHolding(Long portfolioId, CreateHoldingRequest createHoldingRequest, User user);

    List<HoldingResponse> getAllHoldings(Long portfolioId, User user);

    HoldingResponse getHoldingById(Long holdingId, User user);

    HoldingResponse updateHoldingById(Long holdingId, UpdateHoldingRequest updateHoldingRequest, User user);

    void deleteHoldingById(Long holdingId, User user);
}
