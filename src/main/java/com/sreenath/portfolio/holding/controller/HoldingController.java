package com.sreenath.portfolio.holding.controller;

import com.sreenath.portfolio.auth.security.CustomUserDetails;
import com.sreenath.portfolio.holding.dto.CreateHoldingRequest;
import com.sreenath.portfolio.holding.dto.HoldingResponse;
import com.sreenath.portfolio.holding.dto.UpdateHoldingRequest;
import com.sreenath.portfolio.holding.service.HoldingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class HoldingController {

    private final HoldingService holdingService;

    @PostMapping("/portfolios/{portfolioId}/holdings")
    public ResponseEntity<HoldingResponse> createHolding(@PathVariable Long portfolioId,
                                                         @Valid @RequestBody CreateHoldingRequest createHoldingRequest,
                                                         @AuthenticationPrincipal CustomUserDetails customUserDetails){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(holdingService.createHolding(portfolioId,createHoldingRequest,customUserDetails.getUser()));
    }

    @GetMapping("/portfolios/{portfolioId}/holdings")
    public ResponseEntity<List<HoldingResponse>> getAllHoldingsByPortfolioId(@PathVariable Long portfolioId,
                                                                             @AuthenticationPrincipal CustomUserDetails customUserDetails){
        return ResponseEntity.status(HttpStatus.FOUND)
                .body(holdingService.getAllHoldings(portfolioId,customUserDetails.getUser()));
    }

    @GetMapping("/holdings/{holdingId}")
    public ResponseEntity<HoldingResponse> getHoldingById(@PathVariable Long holdingId,
                                                          @AuthenticationPrincipal CustomUserDetails customUserDetails){
        return ResponseEntity.status(HttpStatus.FOUND)
                .body(holdingService.getHoldingById(holdingId,customUserDetails.getUser()));
    }

    @PutMapping("/holdings/{holdingId}")
    public ResponseEntity<HoldingResponse> updateHoldingById(@PathVariable Long holdingId,
                                                             @Valid @RequestBody UpdateHoldingRequest updateHoldingRequest,
                                                             @AuthenticationPrincipal CustomUserDetails customUserDetails){
        return ResponseEntity.status(HttpStatus.OK)
                .body(holdingService.updateHoldingById(holdingId,updateHoldingRequest,customUserDetails.getUser()));
    }

    @DeleteMapping("/holdings/{holdingId}")
    public ResponseEntity<Void> deleteByHoldingId(@PathVariable Long holdingId,
                                                  @AuthenticationPrincipal CustomUserDetails customUserDetails){
        holdingService.deleteHoldingById(holdingId,customUserDetails.getUser());
        return ResponseEntity.noContent().build();
    }
}
