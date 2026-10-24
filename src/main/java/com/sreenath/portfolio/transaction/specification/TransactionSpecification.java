package com.sreenath.portfolio.transaction.specification;

import com.sreenath.portfolio.transaction.entity.Transaction;
import com.sreenath.portfolio.transaction.entity.TransactionType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class TransactionSpecification {

    public static Specification<Transaction> hasPortfolioId(Long portfolioId){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("holding").get("portfolio").get("id"),portfolioId);
    }

    public static Specification<Transaction> hasTransactionType(TransactionType transactionType){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("transactionType"),transactionType);
    }

    public static Specification<Transaction> createdAtAfter(LocalDateTime fromDate){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"),fromDate);
    }

    public static Specification<Transaction> createdAtBefore(LocalDateTime toDate){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"),toDate);
    }
}
