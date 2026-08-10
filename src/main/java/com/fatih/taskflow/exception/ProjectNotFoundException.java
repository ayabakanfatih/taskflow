package com.fatih.taskflow.exception;

public class ProjectNotFoundException extends RuntimeException {

    public ProjectNotFoundException(Long id) {
        super("Project bulunamadı. id: " + id);
    }
}
