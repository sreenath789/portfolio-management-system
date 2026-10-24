package com.sreenath.portfolio.portfolio.entity;

import com.sreenath.portfolio.auth.entity.User;
import com.sreenath.portfolio.holding.entity.Holding;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "portfolios",
uniqueConstraints = {
        @UniqueConstraint(name = "uk_user_portfolio_name",columnNames = {"user_id","portfolio_name"})
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Portfolio name is required")
    private String portfolioName;
    private String description;
    @Column(name = "created_at",updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @Builder.Default
    @OneToMany(mappedBy = "portfolio",
               cascade = CascadeType.ALL,
               orphanRemoval = true,
               fetch = FetchType.LAZY)
    private List<Holding> holdings = new ArrayList<>();

}
