package com.sreenath.portfolio.holding.entity;

import com.sreenath.portfolio.portfolio.entity.Portfolio;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "holdings",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_portfolio_stock",
                        columnNames = {"stock_symbol", "portfolio_id"}
                )
        }
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Holding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false,length = 20)
    private String stockSymbol;
    @Column(nullable = false)
    private Integer quantity;
    @Column(nullable = false,precision = 19, scale = 2)
    private BigDecimal avgBuyPrice;
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id",nullable = false)
    private Portfolio portfolio;
}
