package com.sreenath.portfolio.transaction.repository;

import com.sreenath.portfolio.auth.entity.User;
import com.sreenath.portfolio.holding.entity.Holding;
import com.sreenath.portfolio.transaction.entity.Transaction;
import com.sreenath.portfolio.transaction.entity.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Long>,
        JpaSpecificationExecutor<Transaction> {
    List<Transaction> findByHolding(Holding holding);
    List<Transaction> findByHoldingPortfolioUser(User user);
    Optional<Transaction> findByIdAndHoldingPortfolioUser(Long transactionId,User user);
//    Page<Transaction> findByHoldingPortfolioId(Long portfolioId, Pageable pageable);
//    Page<Transaction> findByHoldingPortfolioIdAndTransactionType(
//            Long portfolioId,
//            TransactionType transactionType,
//            Pageable pageable
//    );
}
