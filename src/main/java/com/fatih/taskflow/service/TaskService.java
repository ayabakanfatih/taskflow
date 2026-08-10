package com.fatih.taskflow.service;

import com.fatih.taskflow.dto.CreateTaskRequest;
import com.fatih.taskflow.dto.UpdateTaskStatusRequest;
import com.fatih.taskflow.exception.TaskNotFoundException;
import com.fatih.taskflow.model.Task;
import com.fatih.taskflow.model.TaskPriority;
import com.fatih.taskflow.model.TaskStatus;
import com.fatih.taskflow.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fatih.taskflow.exception.ProjectNotFoundException;
import com.fatih.taskflow.model.Project;
import com.fatih.taskflow.repository.ProjectRepository;


import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    public TaskService(
        TaskRepository taskRepository,
        ProjectRepository projectRepository) {

    this.taskRepository = taskRepository;
    this.projectRepository = projectRepository;
}

    @Transactional(readOnly = true)
    public List<Task> getTasks() {
        return taskRepository.findAll();
    }

    @Transactional
    public Task createTask(CreateTaskRequest request) {

        TaskPriority priority =
                request.getPriority() == null
                        ? TaskPriority.MEDIUM
                        : request.getPriority();

        Task task = new Task(
                request.getTitle(),
                request.getDescription(),
                TaskStatus.TODO,
                priority
        );

        return taskRepository.save(task);
    }

    @Transactional(readOnly = true)
    public Task getTaskById(Long id) {

        return taskRepository
                .findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    @Transactional
    public Task updateTaskStatus(
            Long id,
            UpdateTaskStatusRequest request) {

        Task task = getTaskById(id);

        task.setStatus(request.getStatus());

        return taskRepository.save(task);
    }

    @Transactional
    public void deleteTask(Long id) {

        Task task = getTaskById(id);

        taskRepository.delete(task);
    }
@Transactional
public Task assignTaskToProject(
        Long taskId,
        Long projectId) {

    Task task = getTaskById(taskId);

    Project project = projectRepository
            .findById(projectId)
            .orElseThrow(
                    () -> new ProjectNotFoundException(projectId)
            );

    task.setProject(project);

    return taskRepository.save(task);
}
@Transactional(readOnly = true)
public List<Task> getTasksByProjectId(Long projectId) {

    if (!projectRepository.existsById(projectId)) {
        throw new ProjectNotFoundException(projectId);
    }

    return taskRepository.findByProject_Id(projectId);
}
}
