package com.t1.testing.service.implementation;

import com.t1.testing.entity.Task;
import com.t1.testing.entity.TaskStatus;
import com.t1.testing.exception.TaskNotFoundException;
import com.t1.testing.exception.TaskStatusException;
import com.t1.testing.repository.TaskRepository;
import com.t1.testing.service.sample.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Override
    public Task createTask(Task task) {
        task.setStatus(TaskStatus.IN_PROGRESS);
        return taskRepository.save(task);
    }

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll(Sort.by("id"));
    }

    @Override
    public Task getTaskById(Long taskId) {
        return taskRepository.findById(taskId).orElseThrow(
                () -> new TaskNotFoundException(
                        String.format("Get task by ID failed: Task with id %d not found", taskId))
        );
    }

    @Override
    public Task updateTaskById(Long taskId, Task task) {
        Task taskEntity = taskRepository.findById(taskId).orElseThrow(
                () -> new TaskNotFoundException(
                        String.format("Update task by ID failed: Task with id %d not found", taskId))
        );

        if (taskEntity.getStatus().equals(TaskStatus.DONE)) {
            throw new TaskStatusException("Update task by ID failed: Can't modify task with status DONE");
        }

        if (task.getTitle() != null) {
            taskEntity.setTitle(task.getTitle());
        }
        if (task.getDescription() != null) {
            taskEntity.setDescription(task.getDescription());
        }
        if (task.getStatus() != null) {
            taskEntity.setStatus(task.getStatus());
        }

        return taskRepository.save(taskEntity);
    }

    @Override
    public void deleteTaskById(Long taskId) {
        taskRepository.deleteById(taskId);
    }
}
