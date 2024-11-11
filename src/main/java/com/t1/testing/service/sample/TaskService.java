package com.t1.testing.service.sample;

import com.t1.testing.entity.Task;

import java.util.List;

public interface TaskService {
    Task createTask(Task task);

    List<Task> getAllTasks();

    Task getTaskById(Long taskId);

    Task updateTaskById(Long taskId, Task task);

    void deleteTaskById(Long taskId);
}
