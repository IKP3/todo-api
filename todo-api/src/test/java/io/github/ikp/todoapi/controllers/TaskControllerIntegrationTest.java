package io.github.ikp.todoapi.controllers;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.ikp.todoapi.TestDataUtil;
import io.github.ikp.todoapi.domain.dto.request.TaskRequestDto;
import io.github.ikp.todoapi.domain.entities.TaskEntity;
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
  private final UserService userService;

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
    UserEntity savedUser = userService.createOrUpdateUser(userEntity);

    TaskRequestDto taskEntity = TestDataUtil.createTestTaskDto();
    String taskJson = objectMapper.writeValueAsString(taskEntity);

    mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/"+savedUser.getId()+"/tasks")
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
    UserEntity savedUser = userService.createOrUpdateUser(userEntity);

    TaskRequestDto taskEntity = TestDataUtil.createTestTaskDto();
    String taskJson = objectMapper.writeValueAsString(taskEntity);

    mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/"+savedUser.getId() + 1+"/tasks")
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
    UserEntity savedUser = userService.createOrUpdateUser(userEntity);

    TaskRequestDto taskEntity = TestDataUtil.createTestTaskDto();
    String taskJson = objectMapper.writeValueAsString(taskEntity);

    mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/"+savedUser.getId()+"/tasks")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(taskJson)
    ).andExpect(
        MockMvcResultMatchers.jsonPath("$.id").isNumber()
    ).andExpect(
        MockMvcResultMatchers.jsonPath("$.description").value(taskEntity.getDescription())
    );

  }
  @Test
  public void testThatGetTaskSuccessfullyReturnsHttp200OkWhenTaskExists()
      throws Exception {
    UserEntity userEntity = TestDataUtil.createTestUser();
    UserEntity savedUser = userService.createOrUpdateUser(userEntity);

    TaskEntity taskEntity = TestDataUtil.createTestTask();
    taskEntity.setUser(savedUser);
    taskService.createOrUpdateTask(taskEntity);

    mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/"+savedUser.getId()+"/tasks/"+taskEntity.getId())
        .accept(MediaType.APPLICATION_JSON)
    ).andExpect(
        MockMvcResultMatchers.status().isOk()
    );
  }
  @Test
  public void testThatGetTaskSuccessfullyReturnsHttp404NotFoundWhenTaskDoesNotExist()
      throws Exception {
    UserEntity userEntity = TestDataUtil.createTestUser();
    UserEntity savedUser = userService.createOrUpdateUser(userEntity);

    TaskEntity taskEntity = TestDataUtil.createTestTask();
    taskEntity.setUser(savedUser);

    TaskEntity savedTask = taskService.createOrUpdateTask(taskEntity);

    mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/"+savedUser.getId()+"/tasks/"+savedTask.getId()+1)
        .accept(MediaType.APPLICATION_JSON)
    ).andExpect(
        MockMvcResultMatchers.status().isNotFound()
    );
  }
  @Test
  public void testThatGetTaskSuccessfullyReturnsTaskWhenTaskExists()
      throws Exception {
    UserEntity userEntity = TestDataUtil.createTestUser();
    UserEntity savedUser = userService.createOrUpdateUser(userEntity);

    TaskEntity taskEntity = TestDataUtil.createTestTask();
    taskEntity.setUser(savedUser);
    taskService.createOrUpdateTask(taskEntity);

    mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/"+savedUser.getId()+"/tasks/"+taskEntity.getId())
        .accept(MediaType.APPLICATION_JSON)
    ).andExpect(
        MockMvcResultMatchers.jsonPath("$.id").isNumber()
    ).andExpect(
        MockMvcResultMatchers.jsonPath("$.description").value(taskEntity.getDescription())
    );
  }

  @Test
  public void testThatGetAllTasksSuccessfullyReturnsHttp200OK()
      throws Exception {
    UserEntity userEntity = TestDataUtil.createTestUser();
    UserEntity savedUser = userService.createOrUpdateUser(userEntity);
    TaskEntity taskEntity = TestDataUtil.createTestTask();
    taskEntity.setUser(savedUser);
    taskService.createOrUpdateTask(taskEntity);

    mockMvc.perform(
        MockMvcRequestBuilders.get("/api/v1/users/"+savedUser.getId()+"/tasks/all")
            .accept(MediaType.APPLICATION_JSON)
    ).andExpect(
        MockMvcResultMatchers.status().isOk()
    );
  }
  @Test
  public void testThatGetAllTasksReturnsListOfTasks()
      throws Exception {
    UserEntity userEntity = TestDataUtil.createTestUser();
    UserEntity savedUser = userService.createOrUpdateUser(userEntity);

    TaskEntity taskEntity = TestDataUtil.createTestTask();
    taskEntity.setUser(savedUser);
    taskService.createOrUpdateTask(taskEntity);

    TaskEntity taskEntity2 = TestDataUtil.createTestTask2();
    taskEntity2.setUser(savedUser);
    taskService.createOrUpdateTask(taskEntity2);

    mockMvc.perform(
        MockMvcRequestBuilders.get("/api/v1/users/"+savedUser.getId()+"/tasks/all")
            .accept(MediaType.APPLICATION_JSON)
    ).andExpect(
        MockMvcResultMatchers.jsonPath("$").isArray()
    ).andExpect(
        MockMvcResultMatchers.jsonPath("$", hasSize(2))
    ).andExpect(
        MockMvcResultMatchers.jsonPath("$[*].id").isNotEmpty()
    ).andExpect(
        MockMvcResultMatchers.jsonPath("$[*].description", hasItem(taskEntity.getDescription())));
  }

  @Test
  public void testThatGetMultipleTasksSuccessfullyReturnsHttp200OK()
      throws Exception {
    UserEntity userEntity = TestDataUtil.createTestUser();
    UserEntity savedUser = userService.createOrUpdateUser(userEntity);
    TaskEntity taskEntity = TestDataUtil.createTestTask();
    taskEntity.setUser(savedUser);
    taskService.createOrUpdateTask(taskEntity);

    mockMvc.perform(
        MockMvcRequestBuilders.get("/api/v1/users/"+savedUser.getId()+"/tasks")
            .accept(MediaType.APPLICATION_JSON)
    ).andExpect(
        MockMvcResultMatchers.status().isOk()
    );
  }
  @Test
  public void testThatGetMultipleTasksReturnsListOfTasks()
      throws Exception {
    UserEntity userEntity = TestDataUtil.createTestUser();
    UserEntity savedUser = userService.createOrUpdateUser(userEntity);

    TaskEntity taskEntity = TestDataUtil.createTestTask();
    taskEntity.setUser(savedUser);
    taskService.createOrUpdateTask(taskEntity);

    TaskEntity taskEntity2 = TestDataUtil.createTestTask2();
    taskEntity2.setUser(savedUser);
    taskService.createOrUpdateTask(taskEntity2);

    mockMvc.perform(
        MockMvcRequestBuilders.get("/api/v1/users/"+savedUser.getId()+"/tasks")
            .accept(MediaType.APPLICATION_JSON)
    ).andExpect(
        MockMvcResultMatchers.jsonPath("$.content").isArray()
    ).andExpect(
        MockMvcResultMatchers.jsonPath("$.content", hasSize(2))
    ).andExpect(
        MockMvcResultMatchers.jsonPath("$.content[*].id").isNotEmpty()
    ).andExpect(
        MockMvcResultMatchers.jsonPath("$.content[*].description", hasItem(taskEntity.getDescription())));
  }

  @Test
  public void testThatUpdateTaskSuccessfullyReturnsHttp200OKWhenTaskExists()
      throws Exception {
    UserEntity savedUserEntity = userService.createOrUpdateUser(TestDataUtil.createTestUser());

    TaskEntity taskEntity = TestDataUtil.createTestTask();
    taskEntity.setUser(savedUserEntity);
    TaskEntity savedTask = taskService.createOrUpdateTask(taskEntity);

    TaskEntity updatedTask = TestDataUtil.createTestTask();
    updatedTask.setUser(savedUserEntity);
    updatedTask.setDescription("Updated Description");

    String userJson = objectMapper.writeValueAsString(updatedTask);
    mockMvc.perform(
        MockMvcRequestBuilders.put("/api/v1/users/"+savedUserEntity.getId()+"/tasks/"+savedTask.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(userJson)
    ).andExpect(
        MockMvcResultMatchers.status().isOk()
    );
  }
  @Test
  public void testThatUpdateTaskSuccessfullyReturnsHttp404NotFoundWhenTaskDoesNotExist()
      throws Exception {
    UserEntity savedUserEntity = userService.createOrUpdateUser(TestDataUtil.createTestUser());

    TaskEntity taskEntity = TestDataUtil.createTestTask();
    taskEntity.setUser(savedUserEntity);
    TaskEntity savedTask = taskService.createOrUpdateTask(taskEntity);

    TaskEntity updatedTask = TestDataUtil.createTestTask();
    updatedTask.setUser(savedUserEntity);
    updatedTask.setDescription("Updated Description");


    String userJson = objectMapper.writeValueAsString(updatedTask);
    mockMvc.perform(
        MockMvcRequestBuilders.put("/api/v1/users/"+savedUserEntity.getId()+"/tasks/"+savedTask.getId()+1)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(userJson)
    ).andExpect(
        MockMvcResultMatchers.status().isNotFound()
    );
  }
  @Test
  public void testThatUpdateTaskSuccessfullyReturnsHttp404NotFoundWhenAuthorDoesNotExist()
      throws Exception {
    UserEntity savedUserEntity = userService.createOrUpdateUser(TestDataUtil.createTestUser());

    TaskEntity taskEntity = TestDataUtil.createTestTask();
    taskEntity.setUser(savedUserEntity);
    TaskEntity savedTask = taskService.createOrUpdateTask(taskEntity);

    TaskEntity updatedTask = TestDataUtil.createTestTask();
    updatedTask.setUser(savedUserEntity);
    updatedTask.setDescription("Updated Description");

    String userJson = objectMapper.writeValueAsString(updatedTask);
    mockMvc.perform(
        MockMvcRequestBuilders.put("/api/v1/users/"+savedUserEntity.getId()+1+"/tasks/"+savedTask.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(userJson)
    ).andExpect(
        MockMvcResultMatchers.status().isNotFound()
    );
  }
  @Test
  public void testThatUpdateUserSuccessfullyUpdatesUser()
  throws Exception {
    UserEntity savedUserEntity = userService.createOrUpdateUser(TestDataUtil.createTestUser());

    TaskEntity taskEntity = TestDataUtil.createTestTask();
    taskEntity.setUser(savedUserEntity);
    TaskEntity savedTask = taskService.createOrUpdateTask(taskEntity);

    TaskEntity updatedTask = TestDataUtil.createTestTask();
    updatedTask.setUser(savedUserEntity);
    updatedTask.setDescription("Updated Description");

    String userJson = objectMapper.writeValueAsString(updatedTask);
    mockMvc.perform(
        MockMvcRequestBuilders.put("/api/v1/users/"+savedUserEntity.getId()+"/tasks/"+savedTask.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(userJson)
    ).andExpect(
        MockMvcResultMatchers.jsonPath("$.id").isNumber()
    ).andExpect(
        MockMvcResultMatchers.jsonPath("$.description").value(updatedTask.getDescription())
    );
  }



  @Test
  public void testThatPartialUpdateTaskSuccessfullyReturnsHttp200OKWhenTaskExists()
      throws Exception {
    UserEntity savedUserEntity = userService.createOrUpdateUser(TestDataUtil.createTestUser());

    TaskEntity taskEntity = TestDataUtil.createTestTask();
    taskEntity.setUser(savedUserEntity);

    TaskEntity savedTaskEntity = taskService.createOrUpdateTask(taskEntity);

    TaskRequestDto TaskRequestDto = TestDataUtil.createTestTaskDto();
    TaskRequestDto.setDescription(null);
    TaskRequestDto.setCompleted(true);
    String taskJson = objectMapper.writeValueAsString(TaskRequestDto);

    mockMvc.perform(
        MockMvcRequestBuilders.patch("/api/v1/users/"+savedUserEntity.getId()+"/tasks/"+savedTaskEntity.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(taskJson)
    ).andExpect(
        MockMvcResultMatchers.status().isOk()
    );
  }
  @Test
  public void testThatPartialUpdateTaskSuccessfullyReturnsHttp404NotFoundWhenTaskDoesNotExist()
      throws Exception {
    UserEntity savedUserEntity = userService.createOrUpdateUser(TestDataUtil.createTestUser());

    TaskEntity taskEntity = TestDataUtil.createTestTask();
    taskEntity.setUser(savedUserEntity);
    TaskEntity savedTaskEntity = taskService.createOrUpdateTask(taskEntity);


    TaskRequestDto TaskRequestDto = TestDataUtil.createTestTaskDto();
    TaskRequestDto.setDescription(null);
    TaskRequestDto.setCompleted(true);
    String taskJson = objectMapper.writeValueAsString(TaskRequestDto);

    mockMvc.perform(
        MockMvcRequestBuilders.patch("/api/v1/users/"+savedUserEntity.getId()+"/tasks/"+taskEntity.getId() + 1)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(taskJson)
    ).andExpect(
        MockMvcResultMatchers.status().isNotFound()
    );
  }
  @Test
  public void testThatPartialUpdateTaskSuccessfullyReturnsHttp404NotFoundWhenUserDoesNotExist()
      throws Exception {
    UserEntity savedUserEntity = userService.createOrUpdateUser(TestDataUtil.createTestUser());

    TaskEntity taskEntity = TestDataUtil.createTestTask();
    taskEntity.setUser(savedUserEntity);
    TaskEntity savedTaskEntity = taskService.createOrUpdateTask(taskEntity);


    TaskRequestDto TaskRequestDto = TestDataUtil.createTestTaskDto();
    TaskRequestDto.setDescription(null);
    TaskRequestDto.setCompleted(true);
    String taskJson = objectMapper.writeValueAsString(TaskRequestDto);

    mockMvc.perform(
        MockMvcRequestBuilders.patch("/api/v1/users/"+savedUserEntity.getId()+1+"/tasks/"+taskEntity.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(taskJson)
    ).andExpect(
        MockMvcResultMatchers.status().isNotFound()
    );
  }
  @Test
  public void testThatPartialUpdateTaskSuccessfullyUpdatesTask()
      throws Exception {
    UserEntity savedUserEntity = userService.createOrUpdateUser(TestDataUtil.createTestUser());

    TaskEntity taskEntity = TestDataUtil.createTestTask();
    taskEntity.setUser(savedUserEntity);

    TaskEntity savedTaskEntity = taskService.createOrUpdateTask(taskEntity);

    TaskRequestDto taskUpdate = TestDataUtil.createTestTaskDto();
    taskUpdate.setDescription(null);
    taskUpdate.setCompleted(true);

    String taskJson = objectMapper.writeValueAsString(taskUpdate);

    mockMvc.perform(
        MockMvcRequestBuilders.patch("/api/v1/users/"+savedUserEntity.getId()+"/tasks/"+savedTaskEntity.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(taskJson)
    ).andExpect(
        MockMvcResultMatchers.jsonPath("$.id").isNumber()
    ).andExpect(
        MockMvcResultMatchers.jsonPath("$.description").value(savedTaskEntity.getDescription())
    ).andExpect(
        MockMvcResultMatchers.jsonPath("$.completed").value(taskUpdate.getCompleted())
    );
  }


  @Test
  public void testThatDeleteTaskSuccessfullyReturnsHttp204NoContentWhenTaskExists()
      throws Exception {
    UserEntity savedUserEntity = userService.createOrUpdateUser(TestDataUtil.createTestUser());

    TaskEntity taskEntity = TestDataUtil.createTestTask();
    taskEntity.setUser(savedUserEntity);
    TaskEntity savedTaskEntity = taskService.createOrUpdateTask(taskEntity);
    mockMvc.perform(
        MockMvcRequestBuilders.delete("/api/v1/users/"+savedUserEntity.getId()+"/tasks/"+savedTaskEntity.getId())
            .accept(MediaType.APPLICATION_JSON)
    ).andExpect(
        MockMvcResultMatchers.status().isNoContent()
    );
  }
  @Test
  public void testThatDeleteTaskSuccessfullyReturnsHttp404NotFoundWhenTaskDoesNotExist()
      throws Exception {
    UserEntity savedUserEntity = userService.createOrUpdateUser(TestDataUtil.createTestUser());

    TaskEntity taskEntity = TestDataUtil.createTestTask();
    taskEntity.setUser(savedUserEntity);
    TaskEntity savedTaskEntity = taskService.createOrUpdateTask(taskEntity);

    mockMvc.perform(
        MockMvcRequestBuilders.delete("/api/v1/users/"+savedUserEntity.getId()+"/tasks/"+savedTaskEntity.getId()+1)
            .accept(MediaType.APPLICATION_JSON)
    ).andExpect(
        MockMvcResultMatchers.status().isNotFound()
    );
  }
  @Test
  public void testThatDeleteTaskSuccessfullyReturnsHttp404NotFoundWhenUserDoesNotExist()
      throws Exception {
    UserEntity savedUserEntity = userService.createOrUpdateUser(TestDataUtil.createTestUser());

    TaskEntity taskEntity = TestDataUtil.createTestTask();
    taskEntity.setUser(savedUserEntity);
    TaskEntity savedTaskEntity = taskService.createOrUpdateTask(taskEntity);

    mockMvc.perform(
        MockMvcRequestBuilders.delete("/api/v1/users/"+savedUserEntity.getId()+1+"/tasks/"+savedTaskEntity.getId())
            .accept(MediaType.APPLICATION_JSON)
    ).andExpect(
        MockMvcResultMatchers.status().isNotFound()
    );
  }
}
