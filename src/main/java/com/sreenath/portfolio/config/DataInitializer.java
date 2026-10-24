package com.sreenath.portfolio.config;

import com.sreenath.portfolio.auth.entity.Role;
import com.sreenath.portfolio.auth.entity.RoleName;
import com.sreenath.portfolio.auth.entity.User;
import com.sreenath.portfolio.auth.repository.RoleRepository;
import com.sreenath.portfolio.auth.repository.UserRepository;
import com.sreenath.portfolio.holding.entity.Holding;
import com.sreenath.portfolio.holding.repository.HoldingRepository;
import com.sreenath.portfolio.portfolio.entity.Portfolio;
import com.sreenath.portfolio.portfolio.repository.PortfolioRepository;
import com.sreenath.portfolio.transaction.entity.Transaction;
import com.sreenath.portfolio.transaction.entity.TransactionType;
import com.sreenath.portfolio.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PortfolioRepository portfolioRepository;
    private final HoldingRepository holdingRepository;
    private final TransactionRepository transactionRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        // If users already exist, don't insert sample data again
        if (userRepository.count() > 0) {
            return;
        }

        // =====================================================
        // 1. ROLES
        // =====================================================

        Role admin = createRole(
                RoleName.ROLE_ADMIN,
                "System Administrator"
        );

        Role trader = createRole(
                RoleName.ROLE_TRADER,
                "Trader"
        );

        Role investor = createRole(
                RoleName.ROLE_INVESTOR,
                "Investor"
        );

        // =====================================================
        // 2. USERS
        // =====================================================

        User sreenath = userRepository.save(
                User.builder()
                        .firstName("Sreenath")
                        .lastName("Golla")
                        .email("sreenath@test.com")
                        .password(passwordEncoder.encode("Test@123"))
                        .enabled(true)
                        .roles(Set.of(admin))
                        .build()
        );

        User rahul = userRepository.save(
                User.builder()
                        .firstName("Rahul")
                        .lastName("Sharma")
                        .email("rahul@test.com")
                        .password(passwordEncoder.encode("Test@123"))
                        .enabled(true)
                        .roles(Set.of(investor))
                        .build()
        );

        User priya = userRepository.save(
                User.builder()
                        .firstName("Priya")
                        .lastName("Reddy")
                        .email("priya@test.com")
                        .password(passwordEncoder.encode("Test@123"))
                        .enabled(true)
                        .roles(Set.of(trader))
                        .build()
        );

        // =====================================================
        // 3. PORTFOLIOS
        // =====================================================

        Portfolio sreenathLongTerm = portfolioRepository.save(
                Portfolio.builder()
                        .portfolioName("Long Term")
                        .description("Long term investments")
                        .user(sreenath)
                        .build()
        );

        Portfolio sreenathTrading = portfolioRepository.save(
                Portfolio.builder()
                        .portfolioName("Trading")
                        .description("Short term trading")
                        .user(sreenath)
                        .build()
        );

        Portfolio rahulLongTerm = portfolioRepository.save(
                Portfolio.builder()
                        .portfolioName("Long Term")
                        .description("Retirement portfolio")
                        .user(rahul)
                        .build()
        );

        Portfolio rahulTrading = portfolioRepository.save(
                Portfolio.builder()
                        .portfolioName("Trading")
                        .description("Active trading portfolio")
                        .user(rahul)
                        .build()
        );

        Portfolio priyaLongTerm = portfolioRepository.save(
                Portfolio.builder()
                        .portfolioName("Long Term")
                        .description("Long term wealth creation")
                        .user(priya)
                        .build()
        );

        Portfolio priyaTrading = portfolioRepository.save(
                Portfolio.builder()
                        .portfolioName("Trading")
                        .description("Short term trades")
                        .user(priya)
                        .build()
        );

        // =====================================================
        // 4. HOLDINGS
        // =====================================================

        Holding sreenathTcs = createHolding(
                "TCS",
                15,
                "3200",
                sreenathLongTerm
        );

        Holding sreenathInfy = createHolding(
                "INFY",
                30,
                "1500",
                sreenathLongTerm
        );

        Holding sreenathReliance = createHolding(
                "RELIANCE",
                15,
                "2800",
                sreenathLongTerm
        );

        Holding sreenathHdfc = createHolding(
                "HDFCBANK",
                15,
                "1650",
                sreenathTrading
        );

        Holding sreenathIcici = createHolding(
                "ICICIBANK",
                40,
                "1200",
                sreenathTrading
        );

        Holding rahulTcs = createHolding(
                "TCS",
                10,
                "3100",
                rahulLongTerm
        );

        Holding rahulItc = createHolding(
                "ITC",
                100,
                "450",
                rahulLongTerm
        );

        Holding rahulSbin = createHolding(
                "SBIN",
                0,
                "800",
                rahulTrading
        );

        Holding rahulHdfc = createHolding(
                "HDFCBANK",
                20,
                "1600",
                rahulTrading
        );

        Holding priyaInfy = createHolding(
                "INFY",
                25,
                "1450",
                priyaLongTerm
        );

        Holding priyaReliance = createHolding(
                "RELIANCE",
                10,
                "2700",
                priyaLongTerm
        );

        Holding priyaTcs = createHolding(
                "TCS",
                15,
                "3300",
                priyaTrading
        );

        Holding priyaIcici = createHolding(
                "ICICIBANK",
                30,
                "1180",
                priyaTrading
        );

        // =====================================================
        // 5. TRANSACTIONS
        // =====================================================

        // Sreenath - TCS
        createTransaction(
                sreenathTcs,
                20,
                "3200",
                TransactionType.BUY,
                "Initial TCS purchase"
        );

        createTransaction(
                sreenathTcs,
                5,
                "3500",
                TransactionType.SELL,
                "Partial TCS sale"
        );

        // Sreenath - INFY
        createTransaction(
                sreenathInfy,
                30,
                "1500",
                TransactionType.BUY,
                "Initial INFY purchase"
        );

        // Sreenath - RELIANCE
        createTransaction(
                sreenathReliance,
                15,
                "2800",
                TransactionType.BUY,
                "Initial Reliance purchase"
        );

        // Sreenath - HDFCBANK
        createTransaction(
                sreenathHdfc,
                25,
                "1650",
                TransactionType.BUY,
                "Initial HDFC Bank purchase"
        );

        createTransaction(
                sreenathHdfc,
                10,
                "1800",
                TransactionType.SELL,
                "Partial HDFC Bank sale"
        );

        // Sreenath - ICICIBANK
        createTransaction(
                sreenathIcici,
                40,
                "1200",
                TransactionType.BUY,
                "Initial ICICI Bank purchase"
        );

        // Rahul - TCS
        createTransaction(
                rahulTcs,
                10,
                "3100",
                TransactionType.BUY,
                "Initial TCS purchase"
        );

        // Rahul - ITC
        createTransaction(
                rahulItc,
                100,
                "450",
                TransactionType.BUY,
                "Initial ITC purchase"
        );

        // Rahul - SBIN
        createTransaction(
                rahulSbin,
                50,
                "800",
                TransactionType.BUY,
                "Initial SBI purchase"
        );

        createTransaction(
                rahulSbin,
                50,
                "850",
                TransactionType.SELL,
                "Fully exited SBI position"
        );

        // Rahul - HDFCBANK
        createTransaction(
                rahulHdfc,
                20,
                "1600",
                TransactionType.BUY,
                "Initial HDFC Bank purchase"
        );

        // Priya - INFY
        createTransaction(
                priyaInfy,
                25,
                "1450",
                TransactionType.BUY,
                "Initial INFY purchase"
        );

        // Priya - RELIANCE
        createTransaction(
                priyaReliance,
                10,
                "2700",
                TransactionType.BUY,
                "Initial Reliance purchase"
        );

        // Priya - TCS
        createTransaction(
                priyaTcs,
                15,
                "3300",
                TransactionType.BUY,
                "Initial TCS purchase"
        );

        // Priya - ICICIBANK
        createTransaction(
                priyaIcici,
                30,
                "1180",
                TransactionType.BUY,
                "Initial ICICI Bank purchase"
        );

        System.out.println("========================================");
        System.out.println("Sample data inserted successfully!");
        System.out.println("Users      : 3");
        System.out.println("Portfolios : 6");
        System.out.println("Holdings   : 13");
        System.out.println("Transactions: 16");
        System.out.println("========================================");
    }

    // =====================================================
    // ROLE
    // =====================================================

    private Role createRole(
            RoleName roleName,
            String description) {

        return roleRepository.findByRoleName(roleName)
                .orElseGet(() ->
                        roleRepository.save(
                                Role.builder()
                                        .roleName(roleName)
                                        .description(description)
                                        .build()
                        )
                );
    }

    // =====================================================
    // HOLDING
    // =====================================================

    private Holding createHolding(
            String stockSymbol,
            Integer quantity,
            String avgBuyPrice,
            Portfolio portfolio) {

        return holdingRepository.save(
                Holding.builder()
                        .stockSymbol(stockSymbol)
                        .quantity(quantity)
                        .avgBuyPrice(new BigDecimal(avgBuyPrice))
                        .portfolio(portfolio)
                        .build()
        );
    }

    // =====================================================
    // TRANSACTION
    // =====================================================

    private Transaction createTransaction(
            Holding holding,
            Integer quantity,
            String price,
            TransactionType transactionType,
            String remarks) {

        return transactionRepository.save(
                Transaction.builder()
                        .holding(holding)
                        .quantity(quantity)
                        .price(new BigDecimal(price))
                        .transactionType(transactionType)
                        .remarks(remarks)
                        .build()
        );
    }
}