package com.fatih.taskflow.controller;

import com.fatih.taskflow.dto.CreateTaskRequest;
import com.fatih.taskflow.dto.PageResponse;
import com.fatih.taskflow.dto.TaskResponse;
import com.fatih.taskflow.dto.UpdateTaskStatusRequest;
import com.fatih.taskflow.exception.InvalidSortFieldException;
import com.fatih.taskflow.mapper.TaskMapper;
import com.fatih.taskflow.model.Task;
import com.fatih.taskflow.model.TaskStatus;
import com.fatih.taskflow.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private static final Set<String> ALLOWED_SORT_FIELDS =
            Set.of("id", "title", "status", "priority", "createdAt");

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public PageResponse<TaskResponse> getTasks(
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) Long projectId,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {

        validateSort(pageable);

        Page<Task> page = taskService.getTasks(status, projectId, pageable);

        return PageResponse.from(page, TaskMapper::toResponse);
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
            @Valid @RequestBody UpdateTaskStatusRequest request) {

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

    private void validateSort(Pageable pageable) {
        pageable.getSort().forEach(order -> {
            boolean allowed = ALLOWED_SORT_FIELDS.contains(order.getProperty());
            if (allowed == false) {
                throw new InvalidSortFieldException(
                        order.getProperty(), ALLOWED_SORT_FIELDS);
            }
        });
    }
}
