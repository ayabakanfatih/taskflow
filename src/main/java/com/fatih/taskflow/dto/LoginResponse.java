package com.fatih.taskflow.dto;

public record LoginResponse(
        String token,
        String tokenType,
        long expiresInMinutes,
        UserResponse user
) {
}
