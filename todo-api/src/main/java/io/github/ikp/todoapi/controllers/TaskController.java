package io.github.ikp.todoapi.controllers;

import io.github.ikp.todoapi.domain.dto.TaskDto;
import io.github.ikp.todoapi.domain.entities.TaskEntity;
import io.github.ikp.todoapi.mappers.Mapper;
import io.github.ikp.todoapi.repositories.UserRepository;
import io.github.ikp.todoapi.services.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskController {
  private final TaskService taskService;
  private final Mapper<TaskEntity, TaskDto> taskMapper;
  private final UserRepository userRepository;

  public TaskController(final TaskService taskService,final Mapper<TaskEntity, TaskDto> taskMapper,
      UserRepository userRepository) {
    this.taskService = taskService;
    this.taskMapper = taskMapper;
    this.userRepository = userRepository;
  }
  @PostMapping(path = "/users/{userId}/tasks")
  public ResponseEntity<TaskDto> createTask( @PathVariable Long userId,@RequestBody TaskDto taskDto){
    return userRepository.findById(userId)
        .map(user -> {
          TaskEntity task = taskMapper.mapFrom(taskDto);
          task.setUser(user);
          TaskEntity saved = taskService.saveTask(task);
          return new ResponseEntity<>(taskMapper.mapTo(saved), HttpStatus.CREATED);
        })
        .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }
}
