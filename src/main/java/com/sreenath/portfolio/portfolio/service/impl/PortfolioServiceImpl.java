package com.sreenath.portfolio.portfolio.service.impl;

import com.sreenath.portfolio.common.exception.DuplicateResourceException;
import com.sreenath.portfolio.common.exception.ResourceNotFoundException;
import com.sreenath.portfolio.holding.dto.HoldingSummaryResponse;
import com.sreenath.portfolio.holding.entity.Holding;
import com.sreenath.portfolio.holding.repository.HoldingRepository;
import com.sreenath.portfolio.portfolio.dto.PortfolioResponse;
import com.sreenath.portfolio.portfolio.dto.CreatePortfolioRequest;
import com.sreenath.portfolio.auth.entity.User;
import com.sreenath.portfolio.portfolio.dto.PortfolioSummaryResponse;
import com.sreenath.portfolio.portfolio.dto.UpdatePortfolioRequest;
import com.sreenath.portfolio.portfolio.entity.Portfolio;
import com.sreenath.portfolio.portfolio.repository.PortfolioRepository;
import com.sreenath.portfolio.portfolio.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PortfolioServiceImpl implements PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final HoldingRepository holdingRepository;
    @Override
    @Transactional
    public PortfolioResponse createPortfolio(CreatePortfolioRequest portfolioRequest, User user) {
        //duplicate portfolio name check
        if(portfolioRepository.existsByPortfolioNameAndUser(portfolioRequest.getPortfolioName(),user)){
            throw new DuplicateResourceException("Portfolio name already exists");
        }

        Portfolio portfolio = Portfolio.builder()
                .portfolioName(portfolioRequest.getPortfolioName())
                .description(portfolioRequest.getDescription())
                .user(user)
                .build();

        Portfolio savedPortfolio = portfolioRepository.save(portfolio);

        return mapToPortfolioResponse(savedPortfolio);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PortfolioResponse> getMyPortfolios(User user,String portfolioName, Pageable pageable) {
        Page<Portfolio> portfolioPage;
        if(portfolioName == null || portfolioName.isBlank()){
            portfolioPage = portfolioRepository.findByUser(user,pageable);
        }
        else{
            portfolioPage = portfolioRepository.findByUserAndPortfolioNameContainingIgnoreCase(user,
                    portfolioName,pageable);
        }
        return portfolioPage
                .map(this::mapToPortfolioResponse);
    }

    @Override
    public PortfolioResponse getPortfolioById(Long portfolioId, User user) {
        Portfolio portfolio = portfolioRepository.findByIdAndUser(portfolioId,user)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio Id not found"));
        return mapToPortfolioResponse(portfolio);
    }

    @Override
    @Transactional
    public PortfolioResponse updatePortfolioById(Long portfolioId,
                                                 UpdatePortfolioRequest updatePortfolioRequest,
                                                 User user) {
        Portfolio portfolio = portfolioRepository.findByIdAndUser(portfolioId,user)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio Id not found"));

        //if nothing changed
        if(portfolio.getPortfolioName().equals(updatePortfolioRequest.getPortfolioName())
                && portfolio.getDescription().equals(updatePortfolioRequest.getDescription())){
            return mapToPortfolioResponse(portfolio);
        }

        if(!portfolio.getPortfolioName().equals(updatePortfolioRequest.getPortfolioName())
        && portfolioRepository.existsByPortfolioNameAndUser(updatePortfolioRequest.getPortfolioName(), user)){
            throw new DuplicateResourceException("Portfolio name already exists");
        }

        portfolio.setPortfolioName(updatePortfolioRequest.getPortfolioName());
        portfolio.setDescription(updatePortfolioRequest.getDescription());

        return mapToPortfolioResponse(portfolio);
    }

    @Override
    public void deletePortfolioById(Long portfolioId, User user) {
        Portfolio portfolio = portfolioRepository.findByIdAndUser(portfolioId,user)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio Id not found"));

        portfolioRepository.delete(portfolio);
    }

    @Override
    @Transactional(readOnly = true)
    public PortfolioSummaryResponse getPortfolioSummaryById(Long portfolioId, User user) {
        Portfolio portfolio = portfolioRepository.findByIdAndUser(portfolioId,user)
                .orElseThrow(()->new ResourceNotFoundException("Portfolio id not found"));

        List<Holding> holdings = holdingRepository.findByPortfolio(portfolio);

        Integer totalHoldings = holdings.size();
        Integer totalQuantity = holdings.stream()
                .mapToInt(Holding::getQuantity)
                .sum();
        BigDecimal totalInvestmentAmount = holdings.stream()
                .map(holding -> holding.getAvgBuyPrice().multiply(BigDecimal.valueOf(holding.getQuantity())))
                .reduce(BigDecimal.ZERO,BigDecimal::add);
        List<HoldingSummaryResponse> holdingSummaryResponses =
                holdings.stream()
                        .map(holding -> {
                            BigDecimal totalInvestedValue =
                                    holding.getAvgBuyPrice()
                                            .multiply(BigDecimal.valueOf(holding.getQuantity()));

                            return HoldingSummaryResponse.builder()
                                    .stockSymbol(holding.getStockSymbol())
                                    .quantity(holding.getQuantity())
                                    .averageBuyPrice(holding.getAvgBuyPrice())
                                    .totalInvestedValue(totalInvestedValue)
                                    .build();
                        }).toList();

        return PortfolioSummaryResponse.builder()
                .portfolioId(portfolioId)
                .totalHoldings(totalHoldings)
                .totalQuantity(totalQuantity)
                .portfolioName(portfolio.getPortfolioName())
                .totalInvestedAmount(totalInvestmentAmount)
                .holdingSummaryResponses(holdingSummaryResponses)
                .build();
    }

    private PortfolioResponse mapToPortfolioResponse(Portfolio portfolio){
        return PortfolioResponse.builder()
                .portfolioName(portfolio.getPortfolioName())
                .id(portfolio.getId())
                .description(portfolio.getDescription())
                .createdAt(portfolio.getCreatedAt())
                .build();
    }
}
