package com.t1.testing.controller;

import com.t1.testing.dto.TaskCreateDto;
import com.t1.testing.dto.TaskDto;
import com.t1.testing.dto.TaskUpdateDto;
import com.t1.testing.mapper.TaskMapper;
import com.t1.testing.service.sample.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskMapper taskMapper;
    private final TaskService taskService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDto createTask(@RequestBody @Valid TaskCreateDto dto) {
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
    public TaskDto getTaskById(@PathVariable Long id) {
        return taskMapper.taskToDto(taskService.getTaskById(id));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskDto updateTaskById(@PathVariable Long id,
                                  @Valid @RequestBody TaskUpdateDto dto) {
        return taskMapper.taskToDto(taskService.updateTaskById(id, taskMapper.taskUpdateDtoToTask(dto)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTaskById(@PathVariable Long id) {
        taskService.deleteTaskById(id);
    }

}
