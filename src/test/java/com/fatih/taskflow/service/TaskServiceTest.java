package com.fatih.taskflow.service;
import java.util.List;
import com.fatih.taskflow.model.Project;
import com.fatih.taskflow.exception.ProjectNotFoundException;
import com.fatih.taskflow.dto.CreateTaskRequest;
import com.fatih.taskflow.model.Task;
import com.fatih.taskflow.model.TaskStatus;
import com.fatih.taskflow.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import java.util.List;
import com.fatih.taskflow.exception.TaskNotFoundException;
import com.fatih.taskflow.dto.UpdateTaskStatusRequest;
import com.fatih.taskflow.repository.ProjectRepository;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;
   
    @Mock
    private ProjectRepository projectRepository;
   
    @InjectMocks
    private TaskService taskService;

    @Test
    void createTask_shouldCreateTaskWithTodoStatus() {

        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("JUnit öğren");

        taskService.createTask(request);

        ArgumentCaptor<Task> taskCaptor =
                ArgumentCaptor.forClass(Task.class);

        verify(taskRepository).save(taskCaptor.capture());

        Task savedTask = taskCaptor.getValue();

        assertEquals("JUnit öğren", savedTask.getTitle());
        assertEquals(TaskStatus.TODO, savedTask.getStatus());
    }
@Test
void getTaskById_shouldReturnTaskWhenTaskExists() {

    Task task = new Task(
            "Mockito öğren",
            TaskStatus.TODO
    );

    when(taskRepository.findById(1L))
            .thenReturn(Optional.of(task));

    Task result = taskService.getTaskById(1L);

    assertEquals("Mockito öğren", result.getTitle());
    assertEquals(TaskStatus.TODO, result.getStatus());

    verify(taskRepository).findById(1L);
}
@Test
void getTaskById_shouldThrowExceptionWhenTaskDoesNotExist() {

    when(taskRepository.findById(999L))
            .thenReturn(Optional.empty());

    TaskNotFoundException exception = assertThrows(
            TaskNotFoundException.class,
            () -> taskService.getTaskById(999L)
    );

    assertEquals(
            "Task bulunamadı. id: 999",
            exception.getMessage()
    );

    verify(taskRepository).findById(999L);
}
@Test
void updateTaskStatus_shouldUpdateExistingTaskStatus() {

    Task existingTask = new Task(
            "Mockito öğren",
            TaskStatus.TODO
    );

    when(taskRepository.findById(1L))
            .thenReturn(Optional.of(existingTask));

    when(taskRepository.save(existingTask))
            .thenReturn(existingTask);

    UpdateTaskStatusRequest request =
            new UpdateTaskStatusRequest();

    request.setStatus(TaskStatus.DONE);

    Task result =
            taskService.updateTaskStatus(1L, request);

    assertEquals(TaskStatus.DONE, result.getStatus());

    assertSame(existingTask, result);

    verify(taskRepository).findById(1L);
    verify(taskRepository).save(existingTask);
}
@Test
void deleteTask_shouldDeleteExistingTask() {

    Task existingTask = new Task(
            "Silinecek görev",
            TaskStatus.TODO
    );

    when(taskRepository.findById(1L))
            .thenReturn(Optional.of(existingTask));

    taskService.deleteTask(1L);

    verify(taskRepository).findById(1L);
    verify(taskRepository).delete(existingTask);
}
@Test
void getTasksByProjectId_shouldReturnTasksWhenProjectExists() {

    Project project = new Project("Java Öğrenme");

    Task task = new Task(
            "Docker Compose öğren",
            TaskStatus.TODO
    );

    task.setProject(project);

    when(projectRepository.existsById(1L))
            .thenReturn(true);

    when(taskRepository.findByProject_Id(1L))
            .thenReturn(List.of(task));

    List<Task> result =
            taskService.getTasksByProjectId(1L);

    assertEquals(1, result.size());
    assertEquals(
            "Docker Compose öğren",
            result.get(0).getTitle()
    );

    verify(projectRepository)
            .existsById(1L);

    verify(taskRepository)
            .findByProject_Id(1L);
}
@Test
void getTasksByProjectId_shouldThrowExceptionWhenProjectDoesNotExist() {

    when(projectRepository.existsById(999L))
            .thenReturn(false);

    ProjectNotFoundException exception =
            assertThrows(
                    ProjectNotFoundException.class,
                    () -> taskService.getTasksByProjectId(999L)
            );

    assertEquals(
            "Project bulunamadı. id: 999",
            exception.getMessage()
    );

    verify(projectRepository)
            .existsById(999L);

    verify(
            taskRepository,
            never()
    ).findByProject_Id(999L);
}
}
