package io.github.ikp.todoapi.mappers.impl;

import io.github.ikp.todoapi.domain.dto.TaskRequestDto;
import io.github.ikp.todoapi.domain.entities.TaskEntity;
import io.github.ikp.todoapi.mappers.RequestMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class TaskRequestMapperImpl implements RequestMapper<TaskEntity, TaskRequestDto> {
  private final ModelMapper modelMapper;
  public TaskRequestMapperImpl(final ModelMapper modelMapper) {
    this.modelMapper = modelMapper;
  }

  @Override
  public TaskEntity mapFrom(TaskRequestDto taskRequestDto) {
    return modelMapper.map(taskRequestDto, TaskEntity.class);
  }
}
