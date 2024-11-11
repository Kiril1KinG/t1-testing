package com.t1.testing.controller;

import com.t1.testing.dto.TaskCreateDto;
import com.t1.testing.dto.TaskDto;
import com.t1.testing.dto.TaskUpdateDto;
import com.t1.testing.mapper.TaskMapper;
import com.t1.testing.service.sample.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/task")
@RequiredArgsConstructor
@Validated
public class TaskController {

    private final TaskMapper taskMapper;
    private final TaskService taskService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDto createTask(@Valid TaskCreateDto dto) {
        return taskMapper.taskToDto(taskService.createTask(taskMapper.taskCreateDtoToTask(dto)));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TaskDto> getAllTasks() {
        return taskService.getAllTasks().stream()
                .map(taskMapper::taskToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskDto getTaskById(@PathVariable Long taskId) {
        return taskMapper.taskToDto(taskService.getTaskById(taskId));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskDto updateTaskById(@PathVariable Long taskId,
                                  @Valid TaskUpdateDto dto) {
        return taskMapper.taskToDto(taskService.updateTaskById(taskId, taskMapper.taskUpdateDtoToTask(dto)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTaskById(@PathVariable Long taskId) {
        taskService.deleteTaskById(taskId);
    }

}
