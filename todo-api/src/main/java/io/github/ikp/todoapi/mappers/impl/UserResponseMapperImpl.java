package io.github.ikp.todoapi.mappers.impl;

import io.github.ikp.todoapi.domain.dto.response.UserResponseDto;
import io.github.ikp.todoapi.domain.entities.UserEntity;
import io.github.ikp.todoapi.mappers.ResponseMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserResponseMapperImpl implements ResponseMapper<UserEntity, UserResponseDto> {
  private final ModelMapper modelMapper;
  public UserResponseMapperImpl(final ModelMapper modelMapper) {
    this.modelMapper = modelMapper;
  }

  @Override
  public UserResponseDto mapTo(UserEntity userEntity) {
    return modelMapper.map(userEntity, UserResponseDto.class);
  }
}
