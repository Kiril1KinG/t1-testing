package com.t1.testing.service;

import com.t1.testing.entity.Task;
import com.t1.testing.entity.TaskStatus;
import com.t1.testing.exception.TaskNotFoundException;
import com.t1.testing.exception.TaskStatusException;
import com.t1.testing.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    public void createTaskShouldReturnTask() {
        Task task = new Task(null, "title", "desc", null);
        Task expected = new Task(1L, "title", "desc", TaskStatus.IN_PROGRESS);

        when(taskRepository.save(task)).thenReturn(expected);

        Task actual = taskService.createTask(task);
        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(taskRepository, times(1)).save(any());
    }

    @Test
    public void getAllTasksShouldReturnEmptyList() {
        List<Task> expected = new ArrayList<>();

        when(taskRepository.findAll(any(Sort.class))).thenReturn(expected);

        List<Task> actual = taskService.getAllTasks();
        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(taskRepository, times(1)).findAll(any(Sort.class));
    }

    @Test
    public void getAllTasksShouldReturnExpectedList() {
        List<Task> expected = List.of(new Task(1L, "title", "desc", TaskStatus.IN_PROGRESS),
                new Task(2L, "title2", "desc2", TaskStatus.DONE));

        when(taskRepository.findAll(any(Sort.class))).thenReturn(expected);

        List<Task> actual = taskService.getAllTasks();
        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(taskRepository, times(1)).findAll(any(Sort.class));
    }

    @Test
    public void getTaskByIdShouldReturnTaskNotFoundException() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(1L));
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    public void getTaskByIdShouldReturnExpectedTask() {
        Task expected = new Task(1L, "title", "desc", TaskStatus.IN_PROGRESS);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(expected));

        Task actual = taskService.getTaskById(1L);
        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    public void updateTaskByIdShouldThrowTaskNotFoundException() {
        Task task = new Task(null, null, "new desc", null);

        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.updateTaskById(1L, task));
        verify(taskRepository, never()).save(any());
    }

    @Test
    public void updateTaskByIdShouldReturnTaskStatusException() {
        Task task = new Task(null, null, "new desc", null);
        Task taskToUpdate = new Task(1L, "title", "desc", TaskStatus.DONE);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(taskToUpdate));

        assertThrows(TaskStatusException.class, () -> taskService.updateTaskById(1L, task));
        verify(taskRepository, never()).save(task);
    }

    @Test
    public void updateTaskByIdShouldReturnUpdatedTask() {
        Task task = new Task(null, null, "new desc", null);
        Task taskToUpdate = new Task(1L, "title", "desc", TaskStatus.IN_PROGRESS);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(taskToUpdate));
        when(taskRepository.save(taskToUpdate)).thenReturn(taskToUpdate);

        Task actual = taskService.updateTaskById(1L, task);
        assertNotNull(actual);
        assertEquals(actual.getDescription(), task.getDescription());
        verify(taskRepository, times(1)).save(taskToUpdate);
    }

    @Test
    public void deleteTaskByIdShouldCallToDataBase() {
        taskService.deleteTaskById(1L);
        verify(taskRepository, times(1)).deleteById(1L);
    }
}