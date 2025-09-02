package io.github.ikp.todoapi.controllers;

import io.github.ikp.todoapi.domain.dto.UserDto;
import io.github.ikp.todoapi.domain.entities.UserEntity;
import io.github.ikp.todoapi.mappers.Mapper;
import io.github.ikp.todoapi.services.UserService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    UserEntity savedUserEntity = userService.createUpdateUser(userEntity);
    return new ResponseEntity<>(userMapper.mapTo(savedUserEntity), HttpStatus.CREATED);
  }
  @GetMapping(path = "/users/{id}")
  public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
    Optional<UserEntity> foundUser = userService.getUser(id);

    return foundUser.map(userEntity -> {
      UserDto userDto = userMapper.mapTo(userEntity);
      return new ResponseEntity<>(userDto, HttpStatus.OK);
    }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }
  @GetMapping(path = "/users")
  public Page<UserDto> getMultipleUsers(Pageable pageable) {
    Page<UserEntity> users = userService.getMultipleUsers(pageable);
    return users.map(userMapper::mapTo);
  }
  @GetMapping(path = "/users/all")
  public List<UserDto> getAllUsers() {
    List<UserEntity> users = userService.getMultipleUsers();
    return users.stream()
        .map(userMapper::mapTo)
        .collect(Collectors.toList());
  }
  @PutMapping("/users/{id}")
  public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {

    if (!userService.existsById(id)) {
      return ResponseEntity.notFound().build();
    }

    UserEntity userEntity = userMapper.mapFrom(userDto);
    userEntity.setId(id);
    UserEntity updated = userService.createUpdateUser(userEntity);

    return ResponseEntity.ok(userMapper.mapTo(updated));
  }
}
