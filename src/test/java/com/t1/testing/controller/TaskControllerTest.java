package com.t1.testing.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.t1.testing.BaseTestContainer;
import com.t1.testing.dto.TaskCreateDto;
import com.t1.testing.dto.TaskUpdateDto;
import com.t1.testing.entity.Task;
import com.t1.testing.entity.TaskStatus;
import com.t1.testing.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TaskControllerTest extends BaseTestContainer {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void beforeEach() {
        saveTestTasks();
    }

    @Test
    public void createTaskShouldReturnExpectedTaskAndStatusCreated() throws Exception {
        TaskCreateDto body = new TaskCreateDto("some title", "some description");
        mockMvc.perform(
                post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.title", is("some title")))
                .andExpect(jsonPath("$.description", is("some description")))
                .andExpect(jsonPath("$.status", is("IN_PROGRESS")));
    }

    @Test
    public void createTaskShouldThrowValidationExceptionAndReturnApiErrorWithStatusBadRequest() throws Exception {
        TaskCreateDto body = new TaskCreateDto("", "some description");
        mockMvc.perform(
                        post("/tasks")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.reason", is("Validation failed")))
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    public void getAllTasksShouldReturnExpectedTasksAndStatusOk() throws Exception {
        mockMvc.perform(
                        get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("title one")))
                .andExpect(jsonPath("$[0].description", is("description one")))
                .andExpect(jsonPath("$[0].status", is("IN_PROGRESS")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].title", is("title two")))
                .andExpect(jsonPath("$[1].description", is("description two")))
                .andExpect(jsonPath("$[1].status", is("DONE")));
    }

    @Test
    public void getByIdShouldReturnExpectedTask() throws Exception {
        mockMvc.perform(
                        get("/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("title one")))
                .andExpect(jsonPath("$.description", is("description one")))
                .andExpect(jsonPath("$.status", is("IN_PROGRESS")));
    }
    @Test
    public void getByIdShouldThrowValidationExceptionAndReturnApiErrorWithStatusBadRequest() throws Exception {
        mockMvc.perform(
                        get("/tasks/0"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.reason", is("Validation failed")))
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    public void getByIdShouldThrowTaskNotFoundExceptionAndReturnApiErrorWithStatusNotFound() throws Exception {
        mockMvc.perform(
                        get("/tasks/3"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Get task by ID failed: Task with id 3 not found")))
                .andExpect(jsonPath("$.reason", is("Entity not found")))
                .andExpect(jsonPath("$.status", is("NOT_FOUND")))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    public void updateTaskByIdShouldReturnExpectedTask() throws Exception {
        TaskUpdateDto body = new TaskUpdateDto("other title", "other description", null);

        mockMvc.perform(
                        put("/tasks/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("other title")))
                .andExpect(jsonPath("$.description", is("other description")))
                .andExpect(jsonPath("$.status", is("IN_PROGRESS")));
    }

    @Test
    public void updateTaskByIdShouldThrowValidationExceptionAndReturnApiErrorWithStatusBadRequest() throws Exception {
        TaskUpdateDto body = new TaskUpdateDto("new title", "new description", null);

        mockMvc.perform(
                        put("/tasks/0")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.reason", is("Validation failed")))
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.timestamp").exists());


        TaskUpdateDto incorrectBody = new TaskUpdateDto("", "", null);

        mockMvc.perform(
                        put("/tasks/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(incorrectBody)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.reason", is("Validation failed")))
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    public void updateTaskByIdShouldThrowTaskNotFoundExceptionAndReturnApiErrorWithStatusNotFound() throws Exception {
        TaskUpdateDto body = new TaskUpdateDto("new title", "new description", null);

        mockMvc.perform(
                        put("/tasks/3")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Update task by ID failed: Task with id 3 not found")))
                .andExpect(jsonPath("$.reason", is("Entity not found")))
                .andExpect(jsonPath("$.status", is("NOT_FOUND")))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    public void updateTaskByIdShouldThrowTaskStatusExceptionAndReturnApiErrorWithStatusConflict() throws Exception {
        TaskUpdateDto body = new TaskUpdateDto("new title", "new description", null);

        mockMvc.perform(
                        put("/tasks/2")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isConflict())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Update task by ID failed: Can't modify task with status DONE")))
                .andExpect(jsonPath("$.reason", is("Status conflict")))
                .andExpect(jsonPath("$.status", is("CONFLICT")))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    public void deleteTaskByIdShouldReturnStatusOk() throws Exception{
        mockMvc.perform(
                        delete("/tasks/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteTaskByIdShouldThrowValidationExceptionAndReturnApiErrorWithStatusBadRequest() throws Exception {
        mockMvc.perform(
                        delete("/tasks/0"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.reason", is("Validation failed")))
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.timestamp").exists());
    }
    private void saveTestTasks() {
        taskRepository.save(new Task(1L, "title one", "description one", TaskStatus.IN_PROGRESS));
        taskRepository.save(new Task(2L, "title two", "description two", TaskStatus.DONE));
    }
}
