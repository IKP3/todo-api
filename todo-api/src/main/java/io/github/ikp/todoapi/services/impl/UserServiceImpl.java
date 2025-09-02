package io.github.ikp.todoapi.services.impl;

import io.github.ikp.todoapi.domain.entities.UserEntity;
import io.github.ikp.todoapi.repositories.UserRepository;
import io.github.ikp.todoapi.services.UserService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  public UserServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }
  @Override
  public UserEntity createUpdateUser(UserEntity userEntity) {
    return userRepository.save(userEntity);
  }

  @Override
  public Optional<UserEntity> getUser(Long id) {
    return userRepository.findById(id);
  }

  @Override
  public List<UserEntity> getMultipleUsers() {
    return StreamSupport.stream(userRepository
                .findAll()
                .spliterator(),
            false)
        .collect(Collectors.toList());
  }

  @Override
  public Page<UserEntity> getMultipleUsers(Pageable pageable) {
    return userRepository.findAll(pageable);
  }

  @Override
  public boolean existsById(Long id) {
    return userRepository.existsById(id);
  }

}
