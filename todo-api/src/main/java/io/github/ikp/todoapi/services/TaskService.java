package io.github.ikp.todoapi.services;

import io.github.ikp.todoapi.domain.entities.TaskEntity;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public interface TaskService {

  TaskEntity saveTask(TaskEntity task);

  Optional<TaskEntity> getTask(Long userId, Long taskId);
}
