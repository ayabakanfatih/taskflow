package com.fatih.taskflow.controller;

import com.fatih.taskflow.dto.CreateTaskRequest;
import com.fatih.taskflow.dto.TaskResponse;
import com.fatih.taskflow.dto.UpdateTaskStatusRequest;
import com.fatih.taskflow.mapper.TaskMapper;
import com.fatih.taskflow.model.Task;
import com.fatih.taskflow.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<TaskResponse> getTasks() {
        return TaskMapper.toResponseList(taskService.getTasks());
    }

    @GetMapping("/project/{projectId}")
    public List<TaskResponse> getTasksByProjectId(@PathVariable Long projectId) {
        return TaskMapper.toResponseList(taskService.getTasksByProjectId(projectId));
    }

    @GetMapping("/{id}")
    public TaskResponse getTaskById(@PathVariable Long id) {
        return TaskMapper.toResponse(taskService.getTaskById(id));
    }

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(
            @Valid @RequestBody CreateTaskRequest request) {

        Task createdTask = taskService.createTask(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdTask.getId())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(TaskMapper.toResponse(createdTask));
    }

    @PatchMapping("/{id}/status")
    public TaskResponse updateTaskStatus(
            @PathVariable Long id,
            @Valid  @RequestBody UpdateTaskStatusRequest request) {

        return TaskMapper.toResponse(taskService.updateTaskStatus(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{taskId}/project/{projectId}")
    public TaskResponse assignTaskToProject(
            @PathVariable Long taskId,
            @PathVariable Long projectId) {

        return TaskMapper.toResponse(taskService.assignTaskToProject(taskId, projectId));
    }
}
