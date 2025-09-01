package io.github.ikp.todoapi.mappers.impl;

import io.github.ikp.todoapi.domain.dto.UserDto;
import io.github.ikp.todoapi.domain.entities.UserEntity;
import io.github.ikp.todoapi.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl implements Mapper<UserEntity, UserDto> {
  private final ModelMapper modelMapper;
  public UserMapperImpl(final ModelMapper modelMapper) {
    this.modelMapper = modelMapper;
  }

  @Override
  public UserDto mapTo(UserEntity userEntity) {
    return modelMapper.map(userEntity, UserDto.class);
  }

  @Override
  public UserEntity mapFrom(UserDto userDto) {
    return modelMapper.map(userDto, UserEntity.class);
  }
}
