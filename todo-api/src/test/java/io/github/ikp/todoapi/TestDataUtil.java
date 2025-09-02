package io.github.ikp.todoapi;

import io.github.ikp.todoapi.domain.dto.UserDto;
import io.github.ikp.todoapi.domain.entities.UserEntity;

public class TestDataUtil {
  public static UserEntity createTestUser() {
    return UserEntity.builder()
        .name("Igor")
        .build();
  }
  public static UserDto createTestUserDto() {
    return UserDto.builder()
        .name("Igor")
        .build();
  }

}
