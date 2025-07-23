package com.greenmetrik.greenmetrikapi.dto;

public record ChangePasswordRequest(
    String oldPassword,
    String newPassword
) {}
