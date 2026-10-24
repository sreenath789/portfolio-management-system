package com.sreenath.portfolio.transaction.controller;

import com.sreenath.portfolio.auth.security.CustomUserDetails;
import com.sreenath.portfolio.transaction.dto.BuyStockRequest;
import com.sreenath.portfolio.transaction.dto.SellStockRequest;
import com.sreenath.portfolio.transaction.dto.TransactionResponse;
import com.sreenath.portfolio.transaction.entity.Transaction;
import com.sreenath.portfolio.transaction.entity.TransactionType;
import com.sreenath.portfolio.transaction.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/transactions/buy")
    public ResponseEntity<TransactionResponse> buyStock(@Valid @RequestBody BuyStockRequest buyStockRequest,
                                                   @AuthenticationPrincipal CustomUserDetails customUserDetails){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(transactionService.buyStock(buyStockRequest,customUserDetails.getUser()));
    }

    @PostMapping("/transactions/sell")
    public ResponseEntity<TransactionResponse> sellStock(@Valid @RequestBody SellStockRequest sellStockRequest,
                                                   @AuthenticationPrincipal CustomUserDetails customUserDetails){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(transactionService.sellStock(sellStockRequest,customUserDetails.getUser()));
    }

    @GetMapping("/transactions/{transactionId}")
    public ResponseEntity<TransactionResponse> getTransactionById(@PathVariable Long transactionId,
                                                         @AuthenticationPrincipal CustomUserDetails customUserDetails){
        return ResponseEntity.ok(transactionService.getTransactionById(transactionId,
                customUserDetails.getUser()));
    }

    @GetMapping("/portfolios/{portfolioId}/transactions")
    public ResponseEntity<Page<TransactionResponse>> getPortfolioTransactions(
            @PathVariable
            Long portfolioId,
            @RequestParam(defaultValue = "0")
            int page,
            @RequestParam(defaultValue = "10")
            int size,
            @RequestParam(defaultValue = "createdAt")
            String sortBy,
            @RequestParam(defaultValue = "desc")
            String direction,
            @RequestParam(required = false)
            TransactionType transactionType,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime fromDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime toDate,
            @AuthenticationPrincipal CustomUserDetails customUserDetails){

        return ResponseEntity.ok(transactionService.getPortfolioTransactions(portfolioId,
                customUserDetails.getUser(),
                page,
                size,
                sortBy,
                direction,
                transactionType,
                fromDate,
                toDate));
    }
}
