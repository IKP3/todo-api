package io.github.ikp.todoapi.mappers.impl;

import io.github.ikp.todoapi.domain.dto.TaskDto;
import io.github.ikp.todoapi.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskMapperImpl implements Mapper<Task, TaskDto> {
  private final ModelMapper modelMapper;
  public TaskMapperImpl(final ModelMapper modelMapper) {
    this.modelMapper = modelMapper;
  }
  @Override
  public TaskDto mapTo(Task task) {
    return modelMapper.map(task, TaskDto.class);
  }

  @Override
  public Task mapFrom(TaskDto taskDto) {
    return modelMapper.map(taskDto, Task.class);
  }
}
