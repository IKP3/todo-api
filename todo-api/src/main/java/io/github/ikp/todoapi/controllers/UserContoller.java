package io.github.ikp.todoapi.controllers;

import io.github.ikp.todoapi.domain.dto.UserDto;
import io.github.ikp.todoapi.domain.entities.UserEntity;
import io.github.ikp.todoapi.mappers.Mapper;
import io.github.ikp.todoapi.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserContoller {
  private final UserService userService;
  private final Mapper<UserEntity, UserDto> userMapper;
  public UserContoller(final UserService userService, final Mapper<UserEntity, UserDto> userMapper) {
    this.userService = userService;
    this.userMapper = userMapper;
  }
  @PostMapping(path = "/users")
  public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
    UserEntity userEntity = userMapper.mapFrom(userDto);
    UserEntity savedUserEntity = userService.createUser(userEntity);
    return new ResponseEntity<>(userMapper.mapTo(savedUserEntity), HttpStatus.CREATED);
  }
}
