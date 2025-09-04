package io.github.ikp.todoapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.ikp.todoapi.TestDataUtil;
import io.github.ikp.todoapi.domain.dto.TaskDto;
import io.github.ikp.todoapi.domain.entities.UserEntity;
import io.github.ikp.todoapi.services.TaskService;
import io.github.ikp.todoapi.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class TaskControllerIntegrationTest {
  private final MockMvc mockMvc;
  private final ObjectMapper objectMapper;
  private final TaskService taskService;
  private UserService userService;

  @Autowired
  public TaskControllerIntegrationTest(final MockMvc mockMvc, final TaskService taskService, final UserService userService) {
    this.mockMvc = mockMvc;
    this.objectMapper = new ObjectMapper();
    this.taskService = taskService;
    this.userService = userService;
  }
  @Test
  public void testThatSaveTaskSuccessfullyReturnsHttp201Created()
      throws Exception {
    UserEntity userEntity = TestDataUtil.createTestUser();
    UserEntity savedUser = userService.createUpdateUser(userEntity);

    TaskDto taskEntity = TestDataUtil.createTestTaskDto();
    String taskJson = objectMapper.writeValueAsString(taskEntity);

    mockMvc.perform(MockMvcRequestBuilders.post("/users/"+savedUser.getId()+"/tasks")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(taskJson)
    ).andExpect(
        MockMvcResultMatchers.status().isCreated()
    );
  }
  @Test
  public void testThatSaveTaskSuccessfullyReturnsHttp404NotFoundWhenUserDoesNotExist()
      throws Exception {
    UserEntity userEntity = TestDataUtil.createTestUser();
    UserEntity savedUser = userService.createUpdateUser(userEntity);

    TaskDto taskEntity = TestDataUtil.createTestTaskDto();
    String taskJson = objectMapper.writeValueAsString(taskEntity);

    mockMvc.perform(MockMvcRequestBuilders.post("/users/"+savedUser.getId() + 1+"/tasks")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(taskJson)
    ).andExpect(
        MockMvcResultMatchers.status().isNotFound()
    );
  }
  @Test
  public void testThatCreateTaskSuccessfullyReturnsSavedTask()
    throws Exception {
    UserEntity userEntity = TestDataUtil.createTestUser();
    UserEntity savedUser = userService.createUpdateUser(userEntity);

    TaskDto taskEntity = TestDataUtil.createTestTaskDto();
    String taskJson = objectMapper.writeValueAsString(taskEntity);

    mockMvc.perform(MockMvcRequestBuilders.post("/users/"+savedUser.getId()+"/tasks")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(taskJson)
    ).andExpect(
        MockMvcResultMatchers.jsonPath("$.id").isNumber()
    ).andExpect(
        MockMvcResultMatchers.jsonPath("$.description").value(taskEntity.getDescription())
    );

  }
}
