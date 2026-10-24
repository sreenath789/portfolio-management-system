package com.sreenath.portfolio.portfolio.repository;

import com.sreenath.portfolio.auth.entity.User;
import com.sreenath.portfolio.portfolio.entity.Portfolio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio,Long> {

    Page<Portfolio> findByUser(User user,
                               Pageable pageable);

    Page<Portfolio> findByUserAndPortfolioNameContainingIgnoreCase(User user,String portfolioName,
                                                                   Pageable pageable);
    Optional<Portfolio> findByIdAndUser(Long id,User user);

    boolean existsByPortfolioNameAndUser(String portfolioName, User user);
}
