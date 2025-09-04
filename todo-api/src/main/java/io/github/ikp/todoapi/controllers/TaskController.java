package io.github.ikp.todoapi.controllers;

import io.github.ikp.todoapi.domain.dto.TaskDto;
import io.github.ikp.todoapi.domain.entities.TaskEntity;
import io.github.ikp.todoapi.mappers.Mapper;
import io.github.ikp.todoapi.repositories.UserRepository;
import io.github.ikp.todoapi.services.TaskService;
import io.github.ikp.todoapi.services.UserService;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskController {
  private final TaskService taskService;
  private final UserService userService;
  private final Mapper<TaskEntity, TaskDto> taskMapper;

  public TaskController(final TaskService taskService,final UserService userService,final Mapper<TaskEntity, TaskDto> taskMapper) {
    this.taskService = taskService;
    this.taskMapper = taskMapper;
    this.userService = userService;
  }
  @PostMapping(path = "/users/{userId}/tasks")
  public ResponseEntity<TaskDto> createTask( @PathVariable Long userId,@RequestBody TaskDto taskDto){
    return userService.getUser(userId)
        .map(user -> {
          TaskEntity task = taskMapper.mapFrom(taskDto);
          task.setUser(user);
          TaskEntity saved = taskService.saveTask(task);
          return new ResponseEntity<>(taskMapper.mapTo(saved), HttpStatus.CREATED);
        })
        .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }
  @GetMapping(path = "/users/{userId}/tasks/{taskId}")
  public ResponseEntity<TaskDto> getTask(@PathVariable Long userId,@PathVariable Long taskId){
    Optional<TaskEntity> taskEntity = taskService.getTask(userId, taskId);
    return taskEntity.map(task->{
      TaskDto taskDto = taskMapper.mapTo(task);
      return new ResponseEntity<>(taskDto, HttpStatus.OK);
    }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }
}
