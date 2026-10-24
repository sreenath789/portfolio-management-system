package com.sreenath.portfolio.auth.controller;

import com.sreenath.portfolio.auth.entity.User;
import com.sreenath.portfolio.auth.dto.UserProfileResponse;
import com.sreenath.portfolio.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/me")
    public UserProfileResponse me(Authentication authentication){
        User user = userRepository
                .findByEmail(authentication.getName())
                .orElseThrow(()->new UsernameNotFoundException("User Not Found"));


        return UserProfileResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .roles(user.getRoles()
                        .stream()
                        .map(role->role.getRoleName().name())
                        .collect(Collectors.toSet()))
                .build();
    }
}
