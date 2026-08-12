package com.fatih.taskflow.dto;

public record ProjectResponse(
        Long id,
        String name,
        long taskCount
) {
}
