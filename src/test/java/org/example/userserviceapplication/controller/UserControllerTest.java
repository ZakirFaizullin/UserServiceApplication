package org.example.userserviceapplication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.userserviceapplication.dto.UserCreateRequest;
import org.example.userserviceapplication.dto.UserDto;
import org.example.userserviceapplication.mapper.UserMapper;
import org.example.userserviceapplication.model.User;
import org.example.userserviceapplication.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private UserMapper mapper;

    private UserDto testUserDto;
    private UserCreateRequest testRequest;
    private User testUser;

    private static final Long USER_ID = 1L;
    private static final String USER_NAME = "Zakir";
    private static final int USER_AGE = 35;
    private static final String USER_EMAIL = "zakir@example.com";


    @BeforeEach
    void setUp() {
        testUser = new User(USER_ID,
                USER_NAME,
                USER_AGE,
                USER_EMAIL,
                LocalDateTime.now());
        testUserDto = new UserDto(USER_ID,
                USER_NAME,
                USER_AGE,
                USER_EMAIL,
                LocalDateTime.now());
        testRequest = new UserCreateRequest(USER_NAME,
                USER_AGE,
                USER_EMAIL);
    }

    @Test
    void testCreateUser() throws Exception {
        when(userService.createUser(testRequest)).thenReturn(testUser);
        when(mapper.entityToDto(testUser)).thenReturn(testUserDto);

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(USER_ID))
                .andExpect(jsonPath("name").value(USER_NAME))
                .andExpect(jsonPath("age").value(USER_AGE))
                .andExpect(jsonPath("email").value(USER_EMAIL));
    }

    @Test
    void testGetUserById() throws Exception {
        when(userService.findUserById(USER_ID)).thenReturn(testUser);
        when(mapper.entityToDto(testUser)).thenReturn(testUserDto);

        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(USER_ID))
                .andExpect(jsonPath("name").value(USER_NAME))
                .andExpect(jsonPath("age").value(USER_AGE))
                .andExpect(jsonPath("email").value(USER_EMAIL));
    }

    @Test
    void testListAllUsers() throws Exception {
        when(userService.findAllUsers()).thenReturn(List.of(testUser));
        when(mapper.entityToDto(testUser)).thenReturn(testUserDto);

        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(USER_ID))
                .andExpect(jsonPath("$[0].name").value(USER_NAME))
                .andExpect(jsonPath("$[0].age").value(USER_AGE))
                .andExpect(jsonPath("$[0].email").value(USER_EMAIL));
    }

    @Test
    void testUpdateUser() throws Exception {
        when(userService.updateUser(testUser)).thenReturn(testUser);
        when(mapper.entityToDto(testUser)).thenReturn(testUserDto);

        mockMvc.perform(put("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(USER_ID))
                .andExpect(jsonPath("name").value(USER_NAME))
                .andExpect(jsonPath("age").value(USER_AGE))
                .andExpect(jsonPath("email").value(USER_EMAIL));

    }

    @Test
    void testDeleteUser() throws Exception {

        doNothing().when(userService).deleteUser(USER_ID);

        mockMvc.perform(delete("/api/v1/users/{id}", USER_ID))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser(USER_ID);
    }

}
