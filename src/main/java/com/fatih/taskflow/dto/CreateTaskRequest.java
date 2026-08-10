package com.fatih.taskflow.dto;

import com.fatih.taskflow.model.TaskPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateTaskRequest {

    @NotBlank(message = "Task başlığı boş olamaz")
    @Size(max = 120, message = "Task başlığı en fazla 120 karakter olabilir")
    private String title;

    @Size(max = 1000, message = "Açıklama en fazla 1000 karakter olabilir")
    private String description;

    private TaskPriority priority;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }
}
