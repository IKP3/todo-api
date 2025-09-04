package io.github.ikp.todoapi.services;

import io.github.ikp.todoapi.domain.entities.TaskEntity;
import org.springframework.stereotype.Service;

@Service
public interface TaskService {

  TaskEntity saveTask(TaskEntity task);
}
