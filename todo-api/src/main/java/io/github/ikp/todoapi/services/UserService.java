package io.github.ikp.todoapi.services;

import io.github.ikp.todoapi.domain.entities.UserEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
  UserEntity createUser(UserEntity userEntity);

  Optional<UserEntity> getUser(Long id);

  List<UserEntity> getMultipleUsers();
  Page<UserEntity> getMultipleUsers(Pageable pageable);
}
