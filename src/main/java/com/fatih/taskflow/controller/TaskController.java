package com.fatih.taskflow.controller;

import com.fatih.taskflow.dto.CreateTaskRequest;
import com.fatih.taskflow.model.Task;
import com.fatih.taskflow.service.TaskService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;
import com.fatih.taskflow.dto.UpdateTaskStatusRequest;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;


@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<Task> getTasks() {
        return taskService.getTasks();
    }

    @GetMapping("/{id}")
    public Task getTaskById(@PathVariable Long id) {
       return taskService.getTaskById(id);
}
    @PostMapping
public ResponseEntity<Task> createTask(
        @Valid @RequestBody CreateTaskRequest request) {

    Task createdTask = taskService.createTask(request);

    URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(createdTask.getId())
            .toUri();

    return ResponseEntity
            .created(location)
            .body(createdTask);
}
    @PatchMapping("/{id}/status")
public Task updateTaskStatus(
        @PathVariable Long id,
        @RequestBody UpdateTaskStatusRequest request) {

    return taskService.updateTaskStatus(id, request);
}
@DeleteMapping("/{id}")
public ResponseEntity<Void> deleteTask(@PathVariable Long id) {

    taskService.deleteTask(id);

    return ResponseEntity.noContent().build();
}
@PatchMapping("/{taskId}/project/{projectId}")
public Task assignTaskToProject(
        @PathVariable Long taskId,
        @PathVariable Long projectId) {

    return taskService.assignTaskToProject(
            taskId,
            projectId
    );
}
}
