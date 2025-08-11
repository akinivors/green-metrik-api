package com.greenmetrik.greenmetrikapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserUpdateRequest(
    @NotBlank(message = "Full name cannot be blank")
    String fullName,

    @NotBlank(message = "Role cannot be blank")
    String role,

    @NotNull(message = "Unit ID cannot be null")
    Long unitId
) {}
