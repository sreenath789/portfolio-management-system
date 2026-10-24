package com.sreenath.portfolio.holding.repository;

import com.sreenath.portfolio.auth.entity.User;
import com.sreenath.portfolio.holding.entity.Holding;
import com.sreenath.portfolio.portfolio.entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HoldingRepository extends JpaRepository<Holding,Long> {

    List<Holding> findByPortfolio(Portfolio portfolio);
    boolean existsByPortfolioIdAndStockSymbol(Long portfolioId,String stockSymbol);
    Optional<Holding> findByIdAndPortfolio(Long holdingId,Portfolio portfolio);

    Optional<Holding> findByIdAndPortfolioUser(Long holdingId, User user);
    Optional<Holding> findByPortfolioAndStockSymbol(Portfolio portfolio,String stockSymbol);
}
