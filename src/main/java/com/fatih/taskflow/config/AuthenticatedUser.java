package com.fatih.taskflow.config;

public record AuthenticatedUser(
        Long id,
        String email
) {
}
