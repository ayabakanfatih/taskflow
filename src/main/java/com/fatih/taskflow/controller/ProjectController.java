package com.fatih.taskflow.controller;

import com.fatih.taskflow.dto.CreateProjectRequest;
import com.fatih.taskflow.dto.ProjectResponse;
import com.fatih.taskflow.dto.TaskResponse;
import com.fatih.taskflow.mapper.TaskMapper;
import com.fatih.taskflow.model.Project;
import com.fatih.taskflow.model.Task;
import com.fatih.taskflow.service.ProjectService;
import com.fatih.taskflow.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final TaskService taskService;

    public ProjectController(
            ProjectService projectService,
            TaskService taskService) {

        this.projectService = projectService;
        this.taskService = taskService;
    }

    @GetMapping
    public List<ProjectResponse> getProjects() {
        return projectService.getProjectResponses();
    }

    @GetMapping("/{id}")
    public ProjectResponse getProjectById(@PathVariable Long id) {
        return projectService.getProjectResponseById(id);
    }

@PostMapping
    public ResponseEntity<ProjectResponse> createProject(
            @Valid @RequestBody CreateProjectRequest request) {

        Project createdProject = projectService.createProject(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdProject.getId())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(new ProjectResponse(
                        createdProject.getId(),
                        createdProject.getName(),
                        0L));
    }

    @GetMapping("/{projectId}/tasks")
    public List<TaskResponse> getProjectTasks(@PathVariable Long projectId) {
        return TaskMapper.toResponseList(taskService.getTasksByProjectId(projectId));
    }
}
