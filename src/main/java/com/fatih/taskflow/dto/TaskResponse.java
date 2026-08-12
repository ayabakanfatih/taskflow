package com.fatih.taskflow.dto;

import com.fatih.taskflow.model.TaskPriority;
import com.fatih.taskflow.model.TaskStatus;

import java.time.LocalDateTime;

public record TaskResponse(
        Long id,
        String title,
        String description,
        TaskStatus status,
        TaskPriority priority,
        LocalDateTime createdAt,
        ProjectSummaryResponse project
) {
}
