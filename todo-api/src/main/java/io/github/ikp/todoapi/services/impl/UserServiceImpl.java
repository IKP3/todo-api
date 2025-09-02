package io.github.ikp.todoapi.services.impl;

import io.github.ikp.todoapi.domain.entities.UserEntity;
import io.github.ikp.todoapi.repositories.UserRepository;
import io.github.ikp.todoapi.services.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  public UserServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }
  @Override
  public UserEntity createUser(UserEntity userEntity) {
    return userRepository.save(userEntity);
  }
}
