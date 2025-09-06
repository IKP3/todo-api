package io.github.ikp.todoapi.mappers.impl;

import io.github.ikp.todoapi.domain.dto.TaskResponseDto;
import io.github.ikp.todoapi.domain.entities.TaskEntity;
import io.github.ikp.todoapi.mappers.ResponseMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class TaskResponseMapperImpl implements ResponseMapper<TaskEntity, TaskResponseDto> {
  private final ModelMapper modelMapper;
  public TaskResponseMapperImpl(final ModelMapper modelMapper) {
    this.modelMapper = modelMapper;
  }
  @Override
  public TaskResponseDto mapTo(TaskEntity task) {
    return modelMapper.map(task, TaskResponseDto.class);
  }
}
