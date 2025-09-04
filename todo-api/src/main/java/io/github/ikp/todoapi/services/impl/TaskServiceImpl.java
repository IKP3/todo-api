package io.github.ikp.todoapi.services.impl;

import io.github.ikp.todoapi.domain.entities.TaskEntity;
import io.github.ikp.todoapi.repositories.TaskRepository;
import io.github.ikp.todoapi.services.TaskService;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

  @Override
  public Optional<TaskEntity> getTask(Long userId, Long taskId) {
    return taskRepository.findByIdAndUserId(taskId,userId);
  }

  @Override
  public List<TaskEntity> getMultipleTasks(Long userId) {
    return taskRepository.findByUserId(userId);
  }

  @Override
  public Page<TaskEntity> getMultipleTasks(Long userId, Pageable pageable) {
    return taskRepository.findByUserId(userId, pageable);
  }
}
