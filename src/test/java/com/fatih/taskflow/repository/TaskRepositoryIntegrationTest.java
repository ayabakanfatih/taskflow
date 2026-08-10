package com.fatih.taskflow.repository;

import com.fatih.taskflow.model.Project;
import com.fatih.taskflow.model.Task;
import com.fatih.taskflow.model.TaskPriority;
import com.fatih.taskflow.model.TaskStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@Testcontainers
@SpringBootTest
@Transactional
class TaskRepositoryIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres =
        new PostgreSQLContainer("postgres:18");

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    void findByProjectId_shouldReturnTasksBelongingToProject() {

        Project project =
                new Project("Repository Test Project");

        projectRepository.saveAndFlush(project);

        Task task = new Task(
                "Repository test task",
                "Repository sorgusunu test ediyoruz",
                TaskStatus.TODO,
                TaskPriority.HIGH
        );

        task.setProject(project);

        taskRepository.saveAndFlush(task);

        List<Task> result =
                taskRepository.findByProject_Id(
                        project.getId()
                );

        assertFalse(result.isEmpty());

        assertEquals(
                1,
                result.size()
        );

        assertEquals(
                "Repository test task",
                result.get(0).getTitle()
        );

        assertEquals(
                project.getId(),
                result.get(0).getProject().getId()
        );
    }
}
