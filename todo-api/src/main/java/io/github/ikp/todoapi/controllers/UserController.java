package io.github.ikp.todoapi.controllers;

import io.github.ikp.todoapi.domain.dto.request.UserRequestDto;
import io.github.ikp.todoapi.domain.dto.response.UserResponseDto;
import io.github.ikp.todoapi.domain.entities.UserEntity;
import io.github.ikp.todoapi.mappers.RequestMapper;
import io.github.ikp.todoapi.mappers.ResponseMapper;
import io.github.ikp.todoapi.services.UserService;
import jakarta.validation.Valid;
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
public class UserController {
  private final UserService userService;
  private final ResponseMapper<UserEntity, UserResponseDto> userResponseMapper;
  private final RequestMapper<UserEntity, UserRequestDto> userRequestMapper;
  public UserController(final UserService userService, final ResponseMapper<UserEntity, UserResponseDto> userResponseMapper, final RequestMapper<UserEntity, UserRequestDto> userRequestMapper) {
    this.userService = userService;
    this.userResponseMapper = userResponseMapper;
    this.userRequestMapper = userRequestMapper;
  }
  @PostMapping(path = "/users")
  public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto userRequestDto) {
    UserEntity userEntity = userRequestMapper.mapFrom(userRequestDto);
    UserEntity savedUserEntity = userService.createOrUpdateUser(userEntity);
    return new ResponseEntity<>(userResponseMapper.mapTo(savedUserEntity), HttpStatus.CREATED);
  }
  @GetMapping(path = "/users/{userId}")
  public ResponseEntity<UserResponseDto> getUser(@PathVariable Long userId) {
    Optional<UserEntity> foundUser = userService.getUser(userId);

    return foundUser.map(userEntity -> {
      UserResponseDto userResponseDto = userResponseMapper.mapTo(userEntity);
      return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
    }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }
  @GetMapping(path = "/users")
  public Page<UserResponseDto> getMultipleUsers(Pageable pageable) {
    Page<UserEntity> users = userService.getMultipleUsers(pageable);
    return users.map(userResponseMapper::mapTo);
  }
  @GetMapping(path = "/users/all")
  public List<UserResponseDto> getAllUsers() {
    List<UserEntity> users = userService.getMultipleUsers();
    return users.stream()
        .map(userResponseMapper::mapTo)
        .collect(Collectors.toList());
  }
  @PutMapping(path = "/users/{userId}")
  public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long userId, @Valid @RequestBody UserRequestDto userRequestDto) {

    if (!userService.existsById(userId)) {
      return ResponseEntity.notFound().build();
    }
    UserEntity userEntity = userRequestMapper.mapFrom(userRequestDto);
    userEntity.setId(userId);
    UserEntity updated = userService.createOrUpdateUser(userEntity);

    return ResponseEntity.ok(userResponseMapper.mapTo(updated));
  }
  @PatchMapping(path = "/users/{userId}")
  public ResponseEntity<UserResponseDto> partialUpdateUser(@PathVariable Long userId, @Valid @RequestBody UserRequestDto userRequestDto) {

    if (!userService.existsById(userId)) {
      return ResponseEntity.notFound().build();
    }

    UserEntity userEntity = userRequestMapper.mapFrom(userRequestDto);
    userEntity.setId(userId);
    UserEntity updated = userService.partialUpdateUser(userId,userEntity);

    return ResponseEntity.ok(userResponseMapper.mapTo(updated));
  }
  @DeleteMapping(path = "/users/{userId}")
  public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
    if (!userService.existsById(userId)) {
      return ResponseEntity.notFound().build();
    }
    userService.deleteUser(userId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
