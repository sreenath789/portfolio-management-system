package com.sreenath.portfolio.transaction.dto;

import com.sreenath.portfolio.transaction.entity.TransactionType;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionResponse {

    private Long id;
    private String stockSymbol;
    private Integer quantity;
    private BigDecimal price;
    private TransactionType transactionType;
    private LocalDateTime createdAt;

}
