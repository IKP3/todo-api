package io.github.ikp.todoapi.domain.dto;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskResponseDto {

  private  Long id;

  private String description;

  private UserResponseDto user;

  private boolean completed = false;

  private Instant createdAt;

  private Instant updatedAt;
}
