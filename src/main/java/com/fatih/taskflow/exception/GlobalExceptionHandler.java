package com.fatih.taskflow.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;
import com.fatih.taskflow.exception.ProjectNotFoundException;


import java.util.List;


import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleTaskNotFound(
            TaskNotFoundException exception) {

        Map<String, Object> body = Map.of(
                "status", 404,
                "error", "Not Found",
                "message", exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(body);
    }
@ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity<Map<String, Object>> handleValidation(
        MethodArgumentNotValidException exception) {

    List<String> errors = exception
            .getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error ->
                    error.getField() + ": " + error.getDefaultMessage())
            .toList();

    Map<String, Object> body = Map.of(
            "status", 400,
            "error", "Bad Request",
            "messages", errors
    );

    return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(body);
}
@ExceptionHandler(ProjectNotFoundException.class)
public ResponseEntity<Map<String, Object>> handleProjectNotFound(
        ProjectNotFoundException exception) {

    Map<String, Object> body = Map.of(
            "status", 404,
            "error", "Not Found",
            "message", exception.getMessage()
    );

    return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(body);
}
}
