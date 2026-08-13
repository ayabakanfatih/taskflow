package com.fatih.taskflow.service;

import com.fatih.taskflow.dto.CreateTaskRequest;
import com.fatih.taskflow.dto.UpdateTaskStatusRequest;
import com.fatih.taskflow.exception.ProjectNotFoundException;
import com.fatih.taskflow.exception.TaskNotFoundException;
import com.fatih.taskflow.model.Project;
import com.fatih.taskflow.model.Task;
import com.fatih.taskflow.model.TaskPriority;
import com.fatih.taskflow.model.TaskStatus;
import com.fatih.taskflow.repository.ProjectRepository;
import com.fatih.taskflow.repository.TaskRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final CurrentUserService currentUserService;

    public TaskService(
            TaskRepository taskRepository,
            ProjectRepository projectRepository,
            CurrentUserService currentUserService) {

        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.currentUserService = currentUserService;
    }

    @Transactional(readOnly = true)
    public Page<Task> getTasks(TaskStatus status, Long projectId, Pageable pageable) {

        Long ownerId = currentUserService.getCurrentUserId();

        if (status != null && projectId != null) {
            return taskRepository.findByOwner_IdAndStatusAndProject_Id(
                    ownerId, status, projectId, pageable);
        }

        if (status != null) {
            return taskRepository.findByOwner_IdAndStatus(ownerId, status, pageable);
        }

        if (projectId != null) {
            if (!projectRepository.existsById(projectId)) {
                throw new ProjectNotFoundException(projectId);
            }
            return taskRepository.findByOwner_IdAndProject_Id(ownerId, projectId, pageable);
        }

        return taskRepository.findByOwner_Id(ownerId, pageable);
    }

    @Transactional(readOnly = true)
    public Task getTaskById(Long id) {
        Long ownerId = currentUserService.getCurrentUserId();

        return taskRepository
                .findByIdAndOwner_Id(id, ownerId)
                .orElseThrow(() -> new TaskNotFoundException(id));
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

        task.setOwner(currentUserService.getCurrentUserReference());

        return taskRepository.save(task);
    }

    @Transactional
    public Task updateTaskStatus(Long id, UpdateTaskStatusRequest request) {
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
    public Task assignTaskToProject(Long taskId, Long projectId) {

        Task task = getTaskById(taskId);

        Project project = projectRepository
                .findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        task.setProject(project);
        return taskRepository.save(task);
    }

    @Transactional(readOnly = true)
    public List<Task> getTasksByProjectId(Long projectId) {

        if (!projectRepository.existsById(projectId)) {
            throw new ProjectNotFoundException(projectId);
        }

        Long ownerId = currentUserService.getCurrentUserId();
        return taskRepository.findByProject_IdAndOwner_Id(projectId, ownerId);
    }
}
