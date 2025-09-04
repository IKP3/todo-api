package io.github.ikp.todoapi.mappers.impl;

import io.github.ikp.todoapi.domain.dto.TaskDto;
import io.github.ikp.todoapi.domain.entities.TaskEntity;
import io.github.ikp.todoapi.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class TaskMapperImpl implements Mapper<TaskEntity, TaskDto> {
  private final ModelMapper modelMapper;
  public TaskMapperImpl(final ModelMapper modelMapper) {
    this.modelMapper = modelMapper;
  }
  @Override
  public TaskDto mapTo(TaskEntity task) {
    return modelMapper.map(task, TaskDto.class);
  }

  @Override
  public TaskEntity mapFrom(TaskDto taskDto) {
    return modelMapper.map(taskDto, TaskEntity.class);
  }
}
