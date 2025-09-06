package io.github.ikp.todoapi.services;

import io.github.ikp.todoapi.domain.entities.TaskEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface TaskService {

  TaskEntity createOrUpdateTask(TaskEntity task);

  Optional<TaskEntity> getTask(Long userId, Long taskId);

  List<TaskEntity> getMultipleTasks(Long userId);
  Page<TaskEntity> getMultipleTasks(Long userId, Pageable pageable);

  boolean existsByIdAndUserId(Long userId,Long taskId);

  TaskEntity partialUpdate(Long userId,Long taskId,TaskEntity taskEntity);
}
