package com.fatih.taskflow.dto;

import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String email,
        String fullName,
        LocalDateTime createdAt
) {
}
