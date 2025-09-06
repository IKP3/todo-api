package io.github.ikp.todoapi.controllers;

import io.github.ikp.todoapi.domain.dto.TaskDto;
import io.github.ikp.todoapi.domain.entities.TaskEntity;
import io.github.ikp.todoapi.mappers.Mapper;
import io.github.ikp.todoapi.services.TaskService;
import io.github.ikp.todoapi.services.UserService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
          TaskEntity saved = taskService.createOrUpdateTask(task);
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
  @GetMapping(path = "/users/{userId}/tasks/all")
  public List<TaskDto> getAllTasks(@PathVariable Long userId){
    List<TaskEntity> tasks = taskService.getMultipleTasks(userId);
    return tasks.stream()
        .map(taskMapper::mapTo)
        .collect(Collectors.toList());
  }
  @GetMapping(path = "/users/{userId}/tasks")
  public Page<TaskDto> getMultipleTasks(@PathVariable Long userId, Pageable pageable){
    Page<TaskEntity> tasks = taskService.getMultipleTasks(userId,pageable);
    return tasks.map(taskMapper::mapTo);
  }
  @PutMapping(path = "/users/{userId}/tasks/{taskId}")
  public ResponseEntity<TaskDto> updateTask( @PathVariable Long userId, @PathVariable Long taskId, @RequestBody TaskDto taskDto){
    return taskService.getTask(userId,taskId)
        .map(task -> {
          TaskEntity taskEntity = taskMapper.mapFrom(taskDto);
          taskEntity.setId(taskId);
          taskEntity.setUser(task.getUser());
          TaskEntity saved = taskService.createOrUpdateTask(taskEntity);
          return new ResponseEntity<>(taskMapper.mapTo(saved), HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }
  @PatchMapping(path = "/users/{userId}/tasks/{taskId}")
  public ResponseEntity<TaskDto> partialUpdateTask(@PathVariable Long userId, @PathVariable Long taskId, @RequestBody TaskDto taskDto){
    return taskService.getTask(userId,taskId)
        .map(task -> {
          TaskEntity taskEntity = taskMapper.mapFrom(taskDto);
          taskEntity.setUser(task.getUser());
          TaskEntity saved = taskService.partialUpdate(userId, taskId, taskEntity);
          return new ResponseEntity<>(taskMapper.mapTo(saved), HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }
  @DeleteMapping(path = "/users/{userId}/tasks/{taskId}")
  public ResponseEntity<TaskDto> deleteTask(@PathVariable Long userId, @PathVariable Long taskId){
    if (!taskService.existsByIdAndUserId(userId,taskId)) {
      return ResponseEntity.notFound().build();
    }
    taskService.deleteTask(userId, taskId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
