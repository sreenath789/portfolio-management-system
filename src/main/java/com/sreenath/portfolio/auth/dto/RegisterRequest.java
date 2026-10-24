package com.sreenath.portfolio.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "First Name is required")
    @Size(max = 50)
    private String firstName;

    @NotBlank(message = "Last Name is required")
    @Size(max = 50)
    private String lastName;

    @Email(message = "Invalid Email!! Please Enter Valid Email Id")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 20)
    private String password;
}
