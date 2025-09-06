package io.github.ikp.todoapi.services.impl;

import io.github.ikp.todoapi.domain.entities.TaskEntity;
import io.github.ikp.todoapi.repositories.TaskRepository;
import io.github.ikp.todoapi.services.TaskService;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskServiceImpl implements TaskService {
  final TaskRepository taskRepository;
  public TaskServiceImpl(final TaskRepository taskRepository) {
    this.taskRepository = taskRepository;
  }
  @Override
  public TaskEntity createOrUpdateTask(TaskEntity task) {
    return taskRepository.save(task);
  }

  @Override
  public Optional<TaskEntity> getTask(Long userId, Long taskId) {
    return taskRepository.findByIdAndUserId(userId, taskId);
  }

  @Override
  public List<TaskEntity> getMultipleTasks(Long userId) {
    return taskRepository.findByUserId(userId);
  }

  @Override
  public Page<TaskEntity> getMultipleTasks(Long userId, Pageable pageable) {
    return taskRepository.findByUserId(userId, pageable);
  }

  @Override
  public boolean existsByIdAndUserId(Long userId,Long taskId) {
    return taskRepository.existsByUser_IdAndId(userId, taskId);
  }

  @Override
  public TaskEntity partialUpdate(Long userId,Long taskId,TaskEntity taskEntity) {
    taskEntity.setId(taskId);
    return taskRepository.findByIdAndUserId(userId,taskId).map(existingTask ->{
      Optional.ofNullable(taskEntity.getDescription()).ifPresent(existingTask::setDescription);
      Optional.of(taskEntity.isCompleted()).ifPresent(existingTask::setCompleted);
      return taskRepository.save(existingTask);
    }).orElseThrow(()->new RuntimeException("Task does not exist" + taskEntity.getId()));
  }

  @Override
  public void deleteTask(Long userId, Long taskId) {
    taskRepository.deleteByUser_IdAndId(userId,taskId);
  }

}
