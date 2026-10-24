package com.sreenath.portfolio.transaction.dto;

import com.sreenath.portfolio.transaction.entity.TransactionType;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateTransactionRequest {

    @NotBlank
    private String stockSymbol;
    @Positive
    private Integer quantity;
    @Positive
    private BigDecimal price;
    @NotNull
    private TransactionType transactionType;

    @Column(length = 255)
    private String remarks;

}
