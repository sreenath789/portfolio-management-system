package com.sreenath.portfolio.transaction.service;


import com.sreenath.portfolio.auth.entity.User;
import com.sreenath.portfolio.transaction.dto.BuyStockRequest;
import com.sreenath.portfolio.transaction.dto.SellStockRequest;
import com.sreenath.portfolio.transaction.dto.TransactionResponse;
import com.sreenath.portfolio.transaction.entity.TransactionType;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {
    TransactionResponse buyStock(BuyStockRequest buyStockRequest, User user);

    TransactionResponse sellStock(SellStockRequest sellStockRequest, User user);
    Page<TransactionResponse> getPortfolioTransactions(Long portfolioId,
                                                       User user,
                                                       int page,
                                                       int size,
                                                       String sortBy,
                                                       String direction,
                                                       TransactionType transactionType,
                                                       LocalDateTime fromDate,
                                                       LocalDateTime toDate);
    TransactionResponse getTransactionById(Long transactionId,User user);
}
