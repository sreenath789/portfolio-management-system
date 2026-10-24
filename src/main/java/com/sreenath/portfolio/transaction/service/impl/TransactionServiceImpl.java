package com.sreenath.portfolio.transaction.service.impl;

import com.sreenath.portfolio.auth.entity.User;
import com.sreenath.portfolio.common.exception.InsufficientQuantityException;
import com.sreenath.portfolio.common.exception.InvalidRequestException;
import com.sreenath.portfolio.common.exception.ResourceNotFoundException;
import com.sreenath.portfolio.holding.entity.Holding;
import com.sreenath.portfolio.holding.repository.HoldingRepository;
import com.sreenath.portfolio.portfolio.entity.Portfolio;
import com.sreenath.portfolio.portfolio.repository.PortfolioRepository;
import com.sreenath.portfolio.transaction.dto.BuyStockRequest;
import com.sreenath.portfolio.transaction.dto.SellStockRequest;
import com.sreenath.portfolio.transaction.dto.TransactionResponse;
import com.sreenath.portfolio.transaction.entity.Transaction;
import com.sreenath.portfolio.transaction.entity.TransactionType;
import com.sreenath.portfolio.transaction.mapper.TransactionMapper;
import com.sreenath.portfolio.transaction.repository.TransactionRepository;
import com.sreenath.portfolio.transaction.service.TransactionService;
import com.sreenath.portfolio.transaction.specification.TransactionSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final PortfolioRepository portfolioRepository;
    private final HoldingRepository holdingRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    @Override
    @Transactional
    public TransactionResponse buyStock(BuyStockRequest buyStockRequest, User user) {
        //validate portfolio
        Portfolio portfolio = portfolioRepository.findByIdAndUser(buyStockRequest.getPortfolioId(),
                user).orElseThrow(()->new ResourceNotFoundException("Portfolio Id is not found"));

        //normalize stock symbol
        String stockSymbol = buyStockRequest.getStockSymbol().trim().toUpperCase();

        //check holding exists
        Optional<Holding> existingHolding = holdingRepository.findByPortfolioAndStockSymbol(portfolio,
                stockSymbol);

        Holding holding;

        //exists update the holding
        if(existingHolding.isPresent()){
            holding = existingHolding.get();
            BigDecimal oldQuantity = BigDecimal.valueOf(holding.getQuantity());
            BigDecimal oldAverageBuyPrice = holding.getAvgBuyPrice();
            BigDecimal newQuantity = BigDecimal.valueOf(buyStockRequest.getQuantity());
            BigDecimal newPrice = buyStockRequest.getPrice();

            BigDecimal totalQuantity = oldQuantity.add(newQuantity);
            BigDecimal totalCost = oldQuantity.multiply(oldAverageBuyPrice)
                    .add(newQuantity.multiply(newPrice));

            BigDecimal newAveragePrice = totalCost.divide(totalQuantity,MathContext.DECIMAL64);
            holding.setAvgBuyPrice(newAveragePrice);
            holding.setQuantity(totalQuantity.intValue());
        }
        else{
            holding = Holding.builder()
                    .stockSymbol(stockSymbol)
                    .quantity(buyStockRequest.getQuantity())
                    .portfolio(portfolio)
                    .avgBuyPrice(buyStockRequest.getPrice())
                    .createdAt(LocalDateTime.now())
                    .build();

            holding = holdingRepository.save(holding);
        }

        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.BUY)
                .holding(holding)
                .price(buyStockRequest.getPrice())
                .quantity(buyStockRequest.getQuantity())
                .createdAt(LocalDateTime.now())
                .build();

        Transaction savedTransaction = transactionRepository.save(transaction);

        return transactionMapper.toResponse(savedTransaction);
    }

    @Override
    @Transactional
    public TransactionResponse sellStock(SellStockRequest sellStockRequest, User user) {
        Portfolio portfolio = portfolioRepository.findByIdAndUser(sellStockRequest.getPortfolioId(),
                user).orElseThrow(()->new ResourceNotFoundException("Portfolio Id is not found"));

        String stockSymbol = sellStockRequest.getStockSymbol().trim().toUpperCase();

        Holding existingHolding = holdingRepository.findByPortfolioAndStockSymbol(portfolio,
                stockSymbol).orElseThrow(()->new ResourceNotFoundException("Portfolio and StockSymbol doesn't exist"));

        if(sellStockRequest.getQuantity() > existingHolding.getQuantity()){
            throw new InsufficientQuantityException("Selling quantity is more than existing quantity");
        }

        Integer remainingQuantity = existingHolding.getQuantity() - sellStockRequest.getQuantity();

        existingHolding.setQuantity(remainingQuantity);

        Transaction transaction = Transaction.builder()
                .holding(existingHolding)
                .transactionType(TransactionType.SELL)
                .price(sellStockRequest.getPrice())
                .quantity(sellStockRequest.getQuantity())
                .build();

        Transaction savedTransaction = transactionRepository.save(transaction);

        return transactionMapper.toResponse(savedTransaction);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransactionResponse> getPortfolioTransactions(Long portfolioId,
                                                              User user,
                                                              int page,
                                                              int size,
                                                              String sortBy,
                                                              String direction,
                                                              TransactionType transactionType,
                                                              LocalDateTime fromDate,
                                                              LocalDateTime toDate) {

        Portfolio portfolio = portfolioRepository.findByIdAndUser(portfolioId,user)
                .orElseThrow(()->new ResourceNotFoundException("Portfolio Id not found"));

        if(!ALLOWED_SORT_FIELDS.contains(sortBy)){
            throw new InvalidRequestException("Invalid sortBy field: "+sortBy);
        }

        if(!direction.equalsIgnoreCase("desc") && !direction.equalsIgnoreCase("asc")){
            throw new InvalidRequestException("Sort direction would be either asc or desc : "+direction);
        }
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC :
                Sort.Direction.ASC;

        Sort sort = Sort.by(sortDirection,sortBy);

        if(page<0){
            throw new InvalidRequestException("Page number cannot be negative: "+page);
        }
        if(size<=0){
            throw new InvalidRequestException("Page size number must be greater than zero: "+size);
        }

        if(size>500){
            throw new InvalidRequestException("Page size number cannot exceed 500 : "+size);
        }

        Pageable pageable = PageRequest.of(page,size,sort);

        Specification<Transaction> transactionSpecification =
                TransactionSpecification.hasPortfolioId(portfolioId);

        if(transactionType!=null){
            transactionSpecification =
                    transactionSpecification
                            .and(TransactionSpecification.hasTransactionType(transactionType));
        }

        if(fromDate!=null){
            transactionSpecification =
                    transactionSpecification
                            .and(TransactionSpecification.createdAtAfter(fromDate));
        }

        if(toDate!=null){
            transactionSpecification =
                    transactionSpecification
                            .and(TransactionSpecification.createdAtBefore(toDate));
        }

        Page<Transaction> transactions = transactionRepository.findAll(transactionSpecification,pageable);

        return transactions.map(transactionMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public TransactionResponse getTransactionById(Long transactionId, User user) {
        Transaction transaction = transactionRepository
                .findByIdAndHoldingPortfolioUser(transactionId,user)
                .orElseThrow(()->new ResourceNotFoundException("Transaction Id not found"));
        return transactionMapper.toResponse(transaction);
    }

    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of(
            "createdAt",
            "price",
            "quantity",
            "transactionType"
    );
}
