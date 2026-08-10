package com.fatih.taskflow.exception;

public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(Long id) {
        super("Task bulunamadı. id: " + id);
    }
}
