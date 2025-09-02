package io.github.ikp.todoapi.controllers;

import static org.hamcrest.Matchers.hasItem;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.ikp.todoapi.TestDataUtil;
import io.github.ikp.todoapi.domain.dto.UserDto;
import io.github.ikp.todoapi.domain.entities.UserEntity;
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
public class UserControllerIntegrationTest {
  private MockMvc mockMvc;
  private ObjectMapper objectMapper;
  private UserService userService;
  @Autowired
  public UserControllerIntegrationTest(MockMvc mockMvc, UserService userService) {
    this.mockMvc = mockMvc;
    this.objectMapper = new ObjectMapper();
    this.userService = userService;
  }
  @Test
  public void testThatCreateUserSuccessfullyReturnsHttp201Created()
      throws Exception {
    UserDto testUser = TestDataUtil.createTestUserDto();
    String userJson = objectMapper.writeValueAsString(testUser);

    mockMvc.perform(
        MockMvcRequestBuilders.post("/users")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(userJson)
    ).andExpect(
        MockMvcResultMatchers.status().isCreated()
    );
  }
  @Test
  public void testThatCreateUserSuccessfullyReturnsSavedUser()
      throws Exception {
    UserDto testUser = TestDataUtil.createTestUserDto();
    String userJson = objectMapper.writeValueAsString(testUser);

    mockMvc.perform(
        MockMvcRequestBuilders.post("/users")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(userJson)
    ).andExpect(
        MockMvcResultMatchers.jsonPath("$.id").isNumber()
    ).andExpect(
        MockMvcResultMatchers.jsonPath("$.name").value("Igor")
    );
  }
  @Test
  public void testThatGetUserSuccessfullyReturnsHttp200OKWhenUserExists()
      throws Exception {
    UserEntity testUser = TestDataUtil.createTestUser();
    UserEntity savedUser = userService.createUpdateUser(testUser);

    mockMvc.perform(
        MockMvcRequestBuilders.get("/users/"+savedUser.getId())
            .accept(MediaType.APPLICATION_JSON)
    ).andExpect(
        MockMvcResultMatchers.status().isOk()
    );
  }
  @Test
  public void testThatGetUserSuccessfullyReturnsHttp404NotFoundWhenUserDoesNotExist()
      throws Exception {
    mockMvc.perform(
        MockMvcRequestBuilders.get("/users/1")
            .accept(MediaType.APPLICATION_JSON)
    ).andExpect(
        MockMvcResultMatchers.status().isNotFound()
    );
  }
  @Test
  public void testThatGetUserSuccessfullyReturnsUserWhenUserExists()
      throws Exception {
    UserEntity testUser = TestDataUtil.createTestUser();
    UserEntity savedUser = userService.createUpdateUser(testUser);

    mockMvc.perform(
        MockMvcRequestBuilders.get("/users/"+savedUser.getId())
            .accept(MediaType.APPLICATION_JSON)
    ).andExpect(
        MockMvcResultMatchers.jsonPath("$.id").value(savedUser.getId())
    ).andExpect(
        MockMvcResultMatchers.jsonPath("$.name").value(savedUser.getName())
    );
  }
  @Test
  public void testThatGetMultipleUsersSuccessfullyReturnsHttp200OK()
      throws Exception {
    mockMvc.perform(
        MockMvcRequestBuilders.get("/users")
            .accept(MediaType.APPLICATION_JSON)
    ).andExpect(
        MockMvcResultMatchers.status().isOk()
    );
  }
  @Test
  public void testThatMultipleUsersReturnsListOfUsers()
      throws Exception {
    UserEntity userEntity = userService.createUpdateUser(TestDataUtil.createTestUser());
    mockMvc.perform(
        MockMvcRequestBuilders.get("/users")
            .accept(MediaType.APPLICATION_JSON)
    ).andExpect(
        MockMvcResultMatchers.jsonPath("$.content").isArray()
    ).andExpect(
        MockMvcResultMatchers.jsonPath("$.content[*].id").isNotEmpty()
    ).andExpect(
        MockMvcResultMatchers.jsonPath("$.content[*].name", hasItem(userEntity.getName())));
  }
  @Test
  public void testThatGetAllUsersSuccessfullyReturnsHttp200OK()
      throws Exception {
    mockMvc.perform(
        MockMvcRequestBuilders.get("/users/all")
            .accept(MediaType.APPLICATION_JSON)
    ).andExpect(
        MockMvcResultMatchers.status().isOk()
    );
  }

  @Test
  public void testThatGetAllUsersReturnsListOfUsers()
      throws Exception {
    UserEntity userEntity = userService.createUpdateUser(TestDataUtil.createTestUser());
    mockMvc.perform(
        MockMvcRequestBuilders.get("/users/all")
            .accept(MediaType.APPLICATION_JSON)
    ).andExpect(
        MockMvcResultMatchers.jsonPath("$").isArray()
    ).andExpect(
        MockMvcResultMatchers.jsonPath("$[*].id").isNotEmpty()
    ).andExpect(
        MockMvcResultMatchers.jsonPath("$[*].name", hasItem(userEntity.getName())));
  }
  @Test
  public void testThatUpdateUserSuccessfullyReturnsHttp200OKWhenUserExists()
      throws Exception {
    UserEntity savedUserEntity = userService.createUpdateUser(TestDataUtil.createTestUser());
    String userJson = objectMapper.writeValueAsString(UserEntity.builder().name("Rogi").build());
    mockMvc.perform(
        MockMvcRequestBuilders.put("/users/"+savedUserEntity.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(userJson)
    ).andExpect(
        MockMvcResultMatchers.status().isOk()
    );
  }
  @Test
  public void testThatUpdateUserSuccessfullyReturnsHttp404NotFoundWhenUserDoesNotExist()
      throws Exception {
    UserEntity userEntity = UserEntity.builder().id(1L).name("").build();
    String userJson = objectMapper.writeValueAsString(userEntity);
    mockMvc.perform(
        MockMvcRequestBuilders.put("/users/"+userEntity.getId())
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
    UserEntity savedUserEntity = userService.createUpdateUser(TestDataUtil.createTestUser());
    UserEntity updatedUserEntity = UserEntity.builder().id(savedUserEntity.getId()).name("test").build();

    String userJson = objectMapper.writeValueAsString(updatedUserEntity);
    mockMvc.perform(
        MockMvcRequestBuilders.put("/users/"+savedUserEntity.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(userJson)
    ).andExpect(
        MockMvcResultMatchers.jsonPath("$.id").value(savedUserEntity.getId())
    ).andExpect(
        MockMvcResultMatchers.jsonPath("$.name").value(updatedUserEntity.getName())
    );
  }
  @Test
  public void testThatPartialUpdateUserSuccessfullyReturnsHttp200OKWhenUserExists()
      throws Exception {
    UserEntity savedUserEntity = userService.createUpdateUser(TestDataUtil.createTestUser());
    String userJson = objectMapper.writeValueAsString(UserEntity.builder().name("Rogi").build());
    mockMvc.perform(
        MockMvcRequestBuilders.patch("/users/"+savedUserEntity.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(userJson)
    ).andExpect(
        MockMvcResultMatchers.status().isOk()
    );
  }
  @Test
  public void testThatPartialUpdateUserSuccessfullyReturnsHttp404NotFoundWhenUserDoesNotExist()
      throws Exception {
    UserEntity userEntity = UserEntity.builder().id(1L).name("").build();
    String userJson = objectMapper.writeValueAsString(userEntity);
    mockMvc.perform(
        MockMvcRequestBuilders.patch("/users/"+userEntity.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(userJson)
    ).andExpect(
        MockMvcResultMatchers.status().isNotFound()
    );
  }
  @Test
  public void testThatPartialUpdateUserSuccessfullyUpdatesUser()
      throws Exception {
    UserEntity savedUserEntity = userService.createUpdateUser(TestDataUtil.createTestUser());
    UserEntity updatedUserEntity = UserEntity.builder().id(savedUserEntity.getId()).name("test").build();

    String userJson = objectMapper.writeValueAsString(updatedUserEntity);
    mockMvc.perform(
        MockMvcRequestBuilders.patch("/users/"+savedUserEntity.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(userJson)
    ).andExpect(
        MockMvcResultMatchers.jsonPath("$.id").value(savedUserEntity.getId())
    ).andExpect(
        MockMvcResultMatchers.jsonPath("$.name").value(updatedUserEntity.getName())
    );
  }
}
