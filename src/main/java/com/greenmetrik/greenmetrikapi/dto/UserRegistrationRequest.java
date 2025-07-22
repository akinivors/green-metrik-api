package com.greenmetrik.greenmetrikapi.dto;

public record UserRegistrationRequest(
    String username,
    String password,
    String fullName,
    String role,
    Long unitId
) {}
