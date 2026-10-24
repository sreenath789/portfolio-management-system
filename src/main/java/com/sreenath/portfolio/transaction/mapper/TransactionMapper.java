package com.sreenath.portfolio.transaction.mapper;

import com.sreenath.portfolio.transaction.dto.TransactionResponse;
import com.sreenath.portfolio.transaction.entity.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public TransactionResponse toResponse(Transaction transaction){
        return TransactionResponse.builder()
                .id(transaction.getId())
                .price(transaction.getPrice())
                .quantity(transaction.getQuantity())
                .transactionType(transaction.getTransactionType())
                .stockSymbol(transaction.getHolding().getStockSymbol())
                .createdAt(transaction.getCreatedAt())
                .build();
    }
}
