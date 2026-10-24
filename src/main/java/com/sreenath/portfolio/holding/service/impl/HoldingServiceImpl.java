package com.sreenath.portfolio.holding.service.impl;

import com.sreenath.portfolio.auth.entity.User;
import com.sreenath.portfolio.common.exception.DuplicateResourceException;
import com.sreenath.portfolio.common.exception.ResourceNotFoundException;
import com.sreenath.portfolio.holding.dto.CreateHoldingRequest;
import com.sreenath.portfolio.holding.dto.HoldingResponse;
import com.sreenath.portfolio.holding.dto.UpdateHoldingRequest;
import com.sreenath.portfolio.holding.entity.Holding;
import com.sreenath.portfolio.holding.mapper.HoldingMapper;
import com.sreenath.portfolio.holding.repository.HoldingRepository;
import com.sreenath.portfolio.holding.service.HoldingService;
import com.sreenath.portfolio.portfolio.entity.Portfolio;
import com.sreenath.portfolio.portfolio.repository.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HoldingServiceImpl implements HoldingService {

    private final HoldingRepository holdingRepository;
    private final PortfolioRepository portfolioRepository;

    private final HoldingMapper holdingMapper;

    @Override
    @Transactional
    public HoldingResponse createHolding(Long portfolioId, CreateHoldingRequest createHoldingRequest, User user) {
        Portfolio portfolio = portfolioRepository.findByIdAndUser(portfolioId,user)
                .orElseThrow(()->new ResourceNotFoundException("Portfolio Id is not found"));

        String stockSymbol = normalizeStockSymbol(createHoldingRequest.getStockSymbol());

        if(holdingRepository.existsByPortfolioIdAndStockSymbol(portfolioId,stockSymbol)){
            throw new DuplicateResourceException("Holding already exists in this portfolio");
        }

        Holding holding = holdingMapper.toEntity(createHoldingRequest,portfolio,stockSymbol);

        Holding savedHolding = holdingRepository.save(holding);

        return holdingMapper.toResponse(savedHolding);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HoldingResponse> getAllHoldings(Long portfolioId, User user) {
        Portfolio portfolio = portfolioRepository.findByIdAndUser(portfolioId,user)
                .orElseThrow(()->new ResourceNotFoundException("Portfolio Id is not found"));

        List<Holding> holdings = holdingRepository.findByPortfolio(portfolio);

        return holdings
                .stream()
                .map(holdingMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public HoldingResponse getHoldingById(Long holdingId, User user) {
        return holdingMapper.toResponse(
                holdingRepository
                        .findByIdAndPortfolioUser(holdingId,user)
                        .orElseThrow(() -> new ResourceNotFoundException("Holding Id is not found")));
    }

    @Override
    @Transactional
    public HoldingResponse updateHoldingById(Long holdingId, UpdateHoldingRequest updateHoldingRequest, User user) {
        Holding holding = holdingRepository
                .findByIdAndPortfolioUser(holdingId,user)
                .orElseThrow(() -> new ResourceNotFoundException("Holding Id is not found"));

        String stockSymbol = normalizeStockSymbol(updateHoldingRequest.getStockSymbol());

        //if nothing changed
        if(holding.getStockSymbol().equals(stockSymbol) &&
        holding.getQuantity().equals(updateHoldingRequest.getQuantity()) &&
        holding.getAvgBuyPrice().equals(updateHoldingRequest.getAverageBuyPrice())){
            return holdingMapper.toResponse(holding);
        }

        if(!holding.getStockSymbol().equals(stockSymbol) &&
                holdingRepository.existsByPortfolioIdAndStockSymbol(holding.getPortfolio().getId(),stockSymbol)){
            throw new DuplicateResourceException("Stock Symbol already exists in this Holding");
        }

        holding.setStockSymbol(stockSymbol);
        holding.setQuantity(updateHoldingRequest.getQuantity());
        holding.setAvgBuyPrice(updateHoldingRequest.getAverageBuyPrice());

        return holdingMapper.toResponse(holding);
    }

    @Override
    @Transactional
    public void deleteHoldingById(Long holdingId, User user) {
        Holding holding = holdingRepository
                .findByIdAndPortfolioUser(holdingId,user)
                .orElseThrow(() -> new ResourceNotFoundException("Holding Id is not found"));
        holdingRepository.delete(holding);
    }


    private String normalizeStockSymbol(String stockSymbol) {
        return stockSymbol.trim().toUpperCase();
    }

}
