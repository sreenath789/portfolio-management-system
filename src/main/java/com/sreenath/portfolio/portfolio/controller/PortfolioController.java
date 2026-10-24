package com.sreenath.portfolio.portfolio.controller;

import com.sreenath.portfolio.portfolio.dto.PortfolioResponse;
import com.sreenath.portfolio.portfolio.dto.PortfolioSummaryResponse;
import com.sreenath.portfolio.portfolio.dto.UpdatePortfolioRequest;
import com.sreenath.portfolio.portfolio.service.PortfolioService;
import com.sreenath.portfolio.portfolio.dto.CreatePortfolioRequest;
import com.sreenath.portfolio.auth.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/portfolios")
public class PortfolioController {

    private final PortfolioService portfolioService;

    @PostMapping
    public ResponseEntity<PortfolioResponse> createPortfolio(@Valid @RequestBody CreatePortfolioRequest portfolioRequest,
                                                             @AuthenticationPrincipal CustomUserDetails customUserDetails){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        portfolioService.createPortfolio(
                                portfolioRequest,
                                customUserDetails.getUser()
                        )
                );
    }

    @GetMapping
    public ResponseEntity<Page<PortfolioResponse>> getMyPortfolios(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                                   @RequestParam(required = false) String name,
                                                                   Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK)
                .body(portfolioService.getMyPortfolios(customUserDetails.getUser(),name,pageable));
    }

    @GetMapping("/{portfolioId}")
    public ResponseEntity<PortfolioResponse> getPortfolioById(@PathVariable Long portfolioId,
                                                              @AuthenticationPrincipal CustomUserDetails customUserDetails){
        return ResponseEntity.ok(portfolioService.getPortfolioById(portfolioId,customUserDetails.getUser()));
    }

    @PutMapping("/{portfolioId}")
    public ResponseEntity<PortfolioResponse> updatePortfolioById(@PathVariable Long portfolioId,
                                                                 @RequestBody UpdatePortfolioRequest updatePortfolioRequest,
                                                                 @AuthenticationPrincipal CustomUserDetails customUserDetails){
        return ResponseEntity.status(HttpStatus.OK)
                .body(portfolioService.updatePortfolioById(portfolioId,updatePortfolioRequest,customUserDetails.getUser()));
    }

    @DeleteMapping("/{portfolioId}")
    public ResponseEntity<Void> deletePortfolioById(@PathVariable Long portfolioId,
                                                    @AuthenticationPrincipal CustomUserDetails customUserDetails){
        portfolioService.deletePortfolioById(portfolioId,customUserDetails.getUser());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{portfolioId}/summary")
    public ResponseEntity<PortfolioSummaryResponse> getPortfolioSummary(@PathVariable Long portfolioId,
                                                                        @AuthenticationPrincipal CustomUserDetails customUserDetails){
        return ResponseEntity.ok(portfolioService.getPortfolioSummaryById(portfolioId,customUserDetails.getUser()));
    }
}
