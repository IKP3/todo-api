package io.github.ikp.todoapi.services;

import io.github.ikp.todoapi.domain.entities.UserEntity;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
  UserEntity createUser(UserEntity userEntity);

}
