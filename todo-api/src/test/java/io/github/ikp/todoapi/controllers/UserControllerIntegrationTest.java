package io.github.ikp.todoapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.ikp.todoapi.TestDataUtil;
import io.github.ikp.todoapi.domain.dto.UserDto;
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
            .content(userJson)
    ).andExpect(
        MockMvcResultMatchers.jsonPath("$.id").isNumber()
    ).andExpect(
        MockMvcResultMatchers.jsonPath("$.name").value("Igor")
    );
  }

}
