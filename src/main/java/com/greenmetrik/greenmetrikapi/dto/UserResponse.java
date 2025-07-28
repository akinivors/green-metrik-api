package com.greenmetrik.greenmetrikapi.dto;

import com.greenmetrik.greenmetrikapi.model.User;

public record UserResponse(
    Long id,
    String username,
    String fullName,
    String role,
    Long unitId,
    boolean isTemporaryPassword
) {
    public static UserResponse fromEntity(User user) {
        return new UserResponse(
            user.getId(),
            user.getUsername(),
            user.getFullName(),
            user.getRole().name(),
            user.getUnit().getId(),
            user.isTemporaryPassword()
        );
    }
}
