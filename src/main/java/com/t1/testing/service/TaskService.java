package com.t1.testing.service;

import com.t1.testing.entity.Task;
import com.t1.testing.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository repository;

    public Task createTask(Task task) {

    }
}
