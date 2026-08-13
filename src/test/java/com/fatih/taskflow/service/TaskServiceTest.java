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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    private static final Long OWNER_ID = 1L;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private CurrentUserService currentUserService;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    void setUpCurrentUser() {
        lenient().when(currentUserService.getCurrentUserId()).thenReturn(OWNER_ID);
        lenient().when(currentUserService.getCurrentUserReference()).thenReturn(null);
    }

    @Test
    void createTask_shouldCreateTaskWithTodoStatus() {
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("JUnit öğren");
        request.setDescription("Mockito ile birlikte");

        when(taskRepository.save(any(Task.class))).thenAnswer(i -> i.getArgument(0));

        Task result = taskService.createTask(request);

        ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);
        verify(taskRepository).save(captor.capture());

        Task saved = captor.getValue();
        assertEquals("JUnit öğren", saved.getTitle());
        assertEquals(TaskStatus.TODO, saved.getStatus());
        assertEquals(TaskPriority.MEDIUM, saved.getPriority());
        assertSame(saved, result);
    }

    @Test
    void createTask_shouldUseGivenPriority() {
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("Acil iş");
        request.setPriority(TaskPriority.CRITICAL);

        when(taskRepository.save(any(Task.class))).thenAnswer(i -> i.getArgument(0));

        Task result = taskService.createTask(request);

        assertEquals(TaskPriority.CRITICAL, result.getPriority());
    }

    @Test
    void getTaskById_shouldReturnTaskWhenOwnedByCurrentUser() {
        Task task = new Task("Var olan görev", TaskStatus.TODO);
        when(taskRepository.findByIdAndOwner_Id(5L, OWNER_ID))
                .thenReturn(Optional.of(task));

        Task result = taskService.getTaskById(5L);

        assertSame(task, result);
    }

    @Test
    void getTaskById_shouldThrowExceptionWhenTaskDoesNotExist() {
        when(taskRepository.findByIdAndOwner_Id(99L, OWNER_ID))
                .thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(99L));
    }

    @Test
    void getTaskById_shouldThrowExceptionWhenTaskBelongsToAnotherUser() {
        when(taskRepository.findByIdAndOwner_Id(7L, OWNER_ID))
                .thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(7L));

        verify(taskRepository, never()).delete(any(Task.class));
    }

    @Test
    void updateTaskStatus_shouldChangeStatus() {
        Task task = new Task("Görev", TaskStatus.TODO);
        when(taskRepository.findByIdAndOwner_Id(3L, OWNER_ID))
                .thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenAnswer(i -> i.getArgument(0));

        UpdateTaskStatusRequest request = new UpdateTaskStatusRequest();
        request.setStatus(TaskStatus.DONE);

        Task result = taskService.updateTaskStatus(3L, request);

        assertEquals(TaskStatus.DONE, result.getStatus());
    }

    @Test
    void deleteTask_shouldDeleteOwnedTask() {
        Task task = new Task("Silinecek", TaskStatus.TODO);
        when(taskRepository.findByIdAndOwner_Id(4L, OWNER_ID))
                .thenReturn(Optional.of(task));

        taskService.deleteTask(4L);

        verify(taskRepository).delete(task);
    }

    @Test
    void assignTaskToProject_shouldThrowWhenProjectDoesNotExist() {
        Task task = new Task("Görev", TaskStatus.TODO);
        when(taskRepository.findByIdAndOwner_Id(2L, OWNER_ID))
                .thenReturn(Optional.of(task));
        when(projectRepository.findById(404L)).thenReturn(Optional.empty());

        assertThrows(ProjectNotFoundException.class,
                () -> taskService.assignTaskToProject(2L, 404L));

        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void assignTaskToProject_shouldSetProject() {
        Task task = new Task("Görev", TaskStatus.TODO);
        Project project = new Project("Java Öğrenme");

        when(taskRepository.findByIdAndOwner_Id(2L, OWNER_ID))
                .thenReturn(Optional.of(task));
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(taskRepository.save(any(Task.class))).thenAnswer(i -> i.getArgument(0));

        Task result = taskService.assignTaskToProject(2L, 1L);

        assertSame(project, result.getProject());
    }
}
