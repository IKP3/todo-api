package io.github.ikp.todoapi.mappers.impl;

import io.github.ikp.todoapi.domain.dto.TaskRequestDto;
import io.github.ikp.todoapi.domain.dto.UserRequestDto;
import io.github.ikp.todoapi.domain.entities.TaskEntity;
import io.github.ikp.todoapi.domain.entities.UserEntity;
import io.github.ikp.todoapi.mappers.RequestMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserRequestMapperImpl implements RequestMapper<UserEntity, UserRequestDto> {
  private final ModelMapper modelMapper;
  public UserRequestMapperImpl(final ModelMapper modelMapper) {
    this.modelMapper = modelMapper;
  }

  @Override
  public UserEntity mapFrom(UserRequestDto userRequestDto) {
    return modelMapper.map(userRequestDto, UserEntity.class);
  }
}
