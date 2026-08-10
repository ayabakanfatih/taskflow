package com.fatih.taskflow.controller;

import com.fatih.taskflow.model.Task;
import com.fatih.taskflow.model.TaskStatus;
import com.fatih.taskflow.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.fatih.taskflow.exception.TaskNotFoundException;
import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;


@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @Test
    void getTaskById_shouldReturnTask() throws Exception {

        Task task = new Task(
                "JUnit öğren",
                TaskStatus.TODO
        );

        when(taskService.getTaskById(1L))
                .thenReturn(task);

        mockMvc.perform(
                        get("/api/tasks/1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("JUnit öğren"))
                .andExpect(jsonPath("$.status").value("TODO"));
    }
@Test
void getTaskById_shouldReturn404WhenTaskDoesNotExist() throws Exception {

    when(taskService.getTaskById(999L))
            .thenThrow(new TaskNotFoundException(999L));

    mockMvc.perform(
                    get("/api/tasks/999")
            )
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.error").value("Not Found"))
            .andExpect(
                    jsonPath("$.message")
                            .value("Task bulunamadı. id: 999")
            );
}
@Test
void createTask_shouldReturn400WhenTitleIsBlank() throws Exception {

    mockMvc.perform(
                    post("/api/tasks")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                        "title": ""
                                    }
                                    """)
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.error").value("Bad Request"))
            .andExpect(
                    jsonPath("$.messages[0]")
                            .value("title: Task başlığı boş olamaz")
            );
}
@Test
void createTask_shouldReturn201WhenRequestIsValid() throws Exception {

    Task createdTask = new Task(
            "Spring Security öğren",
            TaskStatus.TODO
    );

    when(taskService.createTask(
            org.mockito.ArgumentMatchers.any()
    )).thenReturn(createdTask);

    mockMvc.perform(
                    post("/api/tasks")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                        "title": "Spring Security öğren"
                                    }
                                    """)
            )
            .andExpect(status().isCreated())
            .andExpect(
                    jsonPath("$.title")
                            .value("Spring Security öğren")
            )
            .andExpect(
                    jsonPath("$.status")
                            .value("TODO")
            );
}
@Test
void deleteTask_shouldReturn204WhenTaskIsDeleted() throws Exception {

    mockMvc.perform(
                    delete("/api/tasks/1")
            )
            .andExpect(status().isNoContent());

    verify(taskService).deleteTask(1L);
}
}
