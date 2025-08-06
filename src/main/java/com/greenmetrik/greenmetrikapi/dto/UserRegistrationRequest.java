package com.greenmetrik.greenmetrikapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserRegistrationRequest(
    @NotBlank(message = "Username cannot be blank")
    String username,

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    String password,

    @NotBlank(message = "Full name cannot be blank")
    String fullName,

    @NotBlank(message = "Role cannot be blank")
    String role,

    @NotNull(message = "Unit ID cannot be null")
    Long unitId
) {}
