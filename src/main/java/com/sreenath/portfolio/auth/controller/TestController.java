package com.sreenath.portfolio.auth.controller;

import com.sreenath.portfolio.auth.security.jwt.JwtService;
import com.sreenath.portfolio.auth.dto.JwtValidationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/test")
public class TestController {

    private final JwtService jwtService;

    @GetMapping
    public String testApi(){
        return "Secured Application!";
    }

    @PostMapping("/validate")
    public String validateJwt(@RequestBody JwtValidationRequest jwtValidationRequest){
        return jwtService.extractUserName(jwtValidationRequest.getToken());
    }

    @GetMapping("/public")
    public String publicApi(){
        return "Public API";
    }

    @GetMapping("/investor")
    @PreAuthorize("hasRole('INVESTOR')")
    public String investorApi(){
        return "Investor API";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminApi(){
        return "Admin API";
    }
}
