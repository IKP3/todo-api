package io.github.ikp.todoapi.controllers;

import io.github.ikp.todoapi.domain.dto.request.TaskRequestDto;
import io.github.ikp.todoapi.domain.dto.response.TaskResponseDto;
import io.github.ikp.todoapi.domain.entities.TaskEntity;
import io.github.ikp.todoapi.mappers.RequestMapper;
import io.github.ikp.todoapi.mappers.ResponseMapper;
import io.github.ikp.todoapi.services.TaskService;
import io.github.ikp.todoapi.services.UserService;
import jakarta.validation.Valid;
import java.net.URI;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RequestMapping(path = "/api/v1/users")
@RestController
public class TaskController {
  private final TaskService taskService;
  private final UserService userService;
  private final ResponseMapper<TaskEntity, TaskResponseDto> taskResponseMapper;
  private final RequestMapper<TaskEntity, TaskRequestDto> taskRequestMapper;

  public TaskController(final TaskService taskService,final UserService userService,final ResponseMapper<TaskEntity, TaskResponseDto> taskResponseMapper,final RequestMapper<TaskEntity, TaskRequestDto> taskRequestMapper) {
    this.taskService = taskService;
    this.taskResponseMapper = taskResponseMapper;
    this.userService = userService;
    this.taskRequestMapper = taskRequestMapper;
  }
  @PostMapping(path = "/{userId}/tasks")
  public ResponseEntity<TaskResponseDto> createTask( @PathVariable Long userId, @Valid @RequestBody TaskRequestDto taskRequestDto){
    return userService.getUser(userId)
        .map(user -> {
          TaskEntity task = taskRequestMapper.mapFrom(taskRequestDto);
          task.setUser(user);
          TaskEntity savedTaskEntity = taskService.createOrUpdateTask(task);
          URI location = ServletUriComponentsBuilder
              .fromCurrentRequest()
              .path("/{id}")
              .buildAndExpand(savedTaskEntity.getId())
              .toUri();
          return ResponseEntity.created(location).body(taskResponseMapper.mapTo(savedTaskEntity));
        })
        .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }
  @GetMapping(path = "/{userId}/tasks/{taskId}")
  public ResponseEntity<TaskResponseDto> getTask(@PathVariable Long userId,@PathVariable Long taskId){
    Optional<TaskEntity> taskEntity = taskService.getTask(userId, taskId);
    return taskEntity.map(task->{
      TaskResponseDto taskResponseDto = taskResponseMapper.mapTo(task);
      return new ResponseEntity<>(taskResponseDto, HttpStatus.OK);
    }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }
  @GetMapping(path = "/{userId}/tasks/all")
  public List<TaskResponseDto> getAllTasks(@PathVariable Long userId){
    List<TaskEntity> tasks = taskService.getMultipleTasks(userId);
    return tasks.stream()
        .map(taskResponseMapper::mapTo)
        .collect(Collectors.toList());
  }
  @GetMapping(path = "/{userId}/tasks")
  public Page<TaskResponseDto> getMultipleTasks(@PathVariable Long userId, Pageable pageable){
    Page<TaskEntity> tasks = taskService.getMultipleTasks(userId,pageable);
    return tasks.map(taskResponseMapper::mapTo);
  }
  @PutMapping(path = "/{userId}/tasks/{taskId}")
  public ResponseEntity<TaskResponseDto> updateTask( @PathVariable Long userId, @PathVariable Long taskId, @Valid @RequestBody TaskRequestDto taskRequestDto){
    return taskService.getTask(userId,taskId)
        .map(task -> {
          TaskEntity taskEntity = taskRequestMapper.mapFrom(taskRequestDto);
          taskEntity.setId(taskId);
          taskEntity.setUser(task.getUser());
          TaskEntity saved = taskService.createOrUpdateTask(taskEntity);
          return new ResponseEntity<>(taskResponseMapper.mapTo(saved), HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }
  @PatchMapping(path = "/{userId}/tasks/{taskId}")
  public ResponseEntity<TaskResponseDto> partialUpdateTask(@PathVariable Long userId, @PathVariable Long taskId, @Valid @RequestBody TaskRequestDto taskRequestDto){
    return taskService.getTask(userId,taskId)
        .map(task -> {
          TaskEntity taskEntity = taskRequestMapper.mapFrom(taskRequestDto);
          taskEntity.setUser(task.getUser());
          TaskEntity saved = taskService.partialUpdate(userId, taskId, taskEntity);
          return new ResponseEntity<>(taskResponseMapper.mapTo(saved), HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }
  @DeleteMapping(path = "/{userId}/tasks/{taskId}")
  public ResponseEntity<TaskResponseDto> deleteTask(@PathVariable Long userId, @PathVariable Long taskId){
    if (!taskService.existsByIdAndUserId(userId,taskId)) {
      return ResponseEntity.notFound().build();
    }
    taskService.deleteTask(userId, taskId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
