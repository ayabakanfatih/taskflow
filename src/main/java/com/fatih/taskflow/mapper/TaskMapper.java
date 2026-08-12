package com.fatih.taskflow.mapper;

import com.fatih.taskflow.dto.ProjectSummaryResponse;
import com.fatih.taskflow.dto.TaskResponse;
import com.fatih.taskflow.model.Project;
import com.fatih.taskflow.model.Task;

import java.util.List;

public final class TaskMapper {

    private TaskMapper() {
    }

    public static TaskResponse toResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getCreatedAt(),
                toProjectSummary(task.getProject())
        );
    }

    public static List<TaskResponse> toResponseList(List<Task> tasks) {
        return tasks.stream()
                .map(TaskMapper::toResponse)
                .toList();
    }

    private static ProjectSummaryResponse toProjectSummary(Project project) {
        if (project == null) {
            return null;
        }
        return new ProjectSummaryResponse(project.getId(), project.getName());
    }
}
