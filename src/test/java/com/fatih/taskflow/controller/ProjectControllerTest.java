package com.fatih.taskflow.controller;

import com.fatih.taskflow.exception.ProjectNotFoundException;
import com.fatih.taskflow.model.Project;
import com.fatih.taskflow.model.Task;
import com.fatih.taskflow.model.TaskStatus;
import com.fatih.taskflow.service.ProjectService;
import com.fatih.taskflow.service.TaskService;
import com.fatih.taskflow.service.JwtService;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.boot.security.autoconfigure.web.servlet.ServletWebSecurityAutoConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = ProjectController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class, ServletWebSecurityAutoConfiguration.class})
class ProjectControllerTest {

    @MockitoBean
    private JwtService jwtService;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProjectService projectService;

    @MockitoBean
    private TaskService taskService;

    @Test
    void getProjectTasks_shouldReturnTasksWhenProjectExists()
            throws Exception {

        Project project = new Project("Java Öğrenme");

        Task task = new Task(
                "Docker Compose öğren",
                TaskStatus.TODO
        );

        task.setProject(project);

        when(taskService.getTasksByProjectId(1L))
                .thenReturn(List.of(task));

        mockMvc.perform(
                        get("/api/projects/1/tasks")
                )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$[0].title")
                                .value("Docker Compose öğren")
                )
                .andExpect(
                        jsonPath("$[0].status")
                                .value("TODO")
                )
                .andExpect(
                        jsonPath("$[0].project.name")
                                .value("Java Öğrenme")
                );
    }

    @Test
    void getProjectTasks_shouldReturn404WhenProjectDoesNotExist()
            throws Exception {

        when(taskService.getTasksByProjectId(999L))
                .thenThrow(
                        new ProjectNotFoundException(999L)
                );

        mockMvc.perform(
                        get("/api/projects/999/tasks")
                )
                .andExpect(status().isNotFound())
                .andExpect(
                        jsonPath("$.status")
                                .value(404)
                )
                .andExpect(
                        jsonPath("$.error")
                                .value("Not Found")
                )
                .andExpect(
                        jsonPath("$.message")
                                .value("Project bulunamadı. id: 999")
                );
    }
}
