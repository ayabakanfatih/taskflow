package com.fatih.taskflow.dto;

import com.fatih.taskflow.model.TaskStatus;

public class UpdateTaskStatusRequest {

    private TaskStatus status;

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }
}
