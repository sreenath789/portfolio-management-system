package com.sreenath.portfolio.portfolio.service;

import com.sreenath.portfolio.portfolio.dto.CreatePortfolioRequest;
import com.sreenath.portfolio.portfolio.dto.PortfolioResponse;
import com.sreenath.portfolio.auth.entity.User;
import com.sreenath.portfolio.portfolio.dto.PortfolioSummaryResponse;
import com.sreenath.portfolio.portfolio.dto.UpdatePortfolioRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PortfolioService {
    PortfolioResponse createPortfolio(CreatePortfolioRequest portfolioRequest, User user);

    Page<PortfolioResponse> getMyPortfolios(User user, String porfolioName, Pageable pageable);

    PortfolioResponse getPortfolioById(Long portfolioId, User user);

    PortfolioResponse updatePortfolioById(Long portfolioId, UpdatePortfolioRequest updatePortfolioRequest, User user);

    void deletePortfolioById(Long portfolioId, User user);

    PortfolioSummaryResponse getPortfolioSummaryById(Long portfolioId, User user);
}
