package io.github.ikp.todoapi.services.impl;

import io.github.ikp.todoapi.domain.entities.TaskEntity;
import io.github.ikp.todoapi.repositories.TaskRepository;
import io.github.ikp.todoapi.services.TaskService;
import org.springframework.stereotype.Service;

@Service
public class TaskServiceImpl implements TaskService {
  final TaskRepository taskRepository;
  public TaskServiceImpl(final TaskRepository taskRepository) {
    this.taskRepository = taskRepository;
  }
  @Override
  public TaskEntity saveTask(TaskEntity task) {
    return taskRepository.save(task);
  }
}
