package com.fatih.taskflow.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @Enumerated(EnumType.STRING)
    private TaskPriority priority;
    
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    private LocalDateTime createdAt;

    protected Task() {
    }

    public Task(String title, TaskStatus status) {
        this.title = title;
        this.status = status;
        this.priority = TaskPriority.MEDIUM;
        this.createdAt = LocalDateTime.now();
    }

    public Task(
            String title,
            String description,
            TaskStatus status,
            TaskPriority priority) {

        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }
public Project getProject() {
    return project;
}

public void setProject(Project project) {
    this.project = project;
}
}
