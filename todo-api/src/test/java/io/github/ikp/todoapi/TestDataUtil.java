package io.github.ikp.todoapi;

import io.github.ikp.todoapi.domain.dto.request.TaskRequestDto;
import io.github.ikp.todoapi.domain.dto.request.UserRequestDto;
import io.github.ikp.todoapi.domain.entities.TaskEntity;
import io.github.ikp.todoapi.domain.entities.UserEntity;

public class TestDataUtil {
  public static UserEntity createTestUser() {
    return UserEntity.builder()
        .name("Igor")
        .build();
  }
  public static UserRequestDto createTestUserDto() {
    return UserRequestDto.builder()
        .name("Igor")
        .build();
  }
  public static TaskEntity createTestTask() {
    return TaskEntity.builder()
        .description("test")
        .build();
  }
  public static TaskEntity createTestTask2() {
    return TaskEntity.builder()
        .description("test2")
        .build();
  }
  public static TaskRequestDto createTestTaskDto() {
    return TaskRequestDto.builder()
        .description("test")
        .build();
  }

}
