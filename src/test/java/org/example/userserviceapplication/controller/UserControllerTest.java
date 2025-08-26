package org.example.userserviceapplication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.userserviceapplication.dto.UserCreateRequest;
import org.example.userserviceapplication.dto.UserDto;
import org.example.userserviceapplication.exceptions.UserAlreadyExistsException;
import org.example.userserviceapplication.exceptions.UserNotFoundException;
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

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

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
    private static final String URL = "/api/v1/users";
    private static final Long WRONG_ID = 1001L;


    @BeforeEach
    void setUp() {
        testUser = new User(USER_ID,
                USER_NAME,
                USER_AGE,
                USER_EMAIL,
                null);
        testUserDto = new UserDto(USER_ID,
                USER_NAME,
                USER_AGE,
                USER_EMAIL,
                null);
        testRequest = new UserCreateRequest(USER_NAME,
                USER_AGE,
                USER_EMAIL);
        objectMapper = new ObjectMapper();
    }

    @Test
    void testCreateUser() throws Exception {
        when(userService.createUser(any(UserCreateRequest.class))).thenReturn(testUserDto);
        when(mapper.entityToDto(testUser)).thenReturn(testUserDto);

        mockMvc.perform(post(URL)
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
        when(userService.findUserById(USER_ID)).thenReturn(testUserDto);
        when(mapper.entityToDto(testUser)).thenReturn(testUserDto);

        mockMvc.perform(get(URL + "/{id}", USER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(USER_ID))
                .andExpect(jsonPath("name").value(USER_NAME))
                .andExpect(jsonPath("age").value(USER_AGE))
                .andExpect(jsonPath("email").value(USER_EMAIL));
    }

    @Test
    void testListAllUsers() throws Exception {
        when(userService.findAllUsers()).thenReturn(List.of(testUserDto));
        when(mapper.entityToDto(testUser)).thenReturn(testUserDto);

        mockMvc.perform(get(URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(USER_ID))
                .andExpect(jsonPath("$[0].name").value(USER_NAME))
                .andExpect(jsonPath("$[0].age").value(USER_AGE))
                .andExpect(jsonPath("$[0].email").value(USER_EMAIL));
    }

    @Test
    void testUpdateUser() throws Exception {
        when(userService.updateUser(testUser)).thenReturn(testUserDto);
        when(mapper.entityToDto(testUser)).thenReturn(testUserDto);

        mockMvc.perform(put(URL + "/{id}", USER_ID)
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

        mockMvc.perform(delete(URL + "/{id}", USER_ID))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser(USER_ID);
    }

    @Test
    void createUser_shouldThrowConflict() throws Exception {
        when(userService.createUser(any())).thenThrow(new UserAlreadyExistsException(testRequest.getEmail()));

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testRequest)))
                .andExpect(status().isConflict());
    }

    @Test
    void getUserById_shouldThrowNotFound() throws Exception {
        when(userService.findUserById(WRONG_ID)).thenThrow(new UserNotFoundException(WRONG_ID));

        mockMvc.perform(get(URL + "/{id}", WRONG_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateUser_shouldThrowNotFound() throws Exception {
        testUser.setId(WRONG_ID);
        when(userService.updateUser(testUser)).thenThrow(new UserNotFoundException(testUser.getId()));

        mockMvc.perform(put(URL + "/{id}", WRONG_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testRequest)))
                .andExpect(status().isNotFound());

    }

    @Test
    void updateUser_shouldThrowConflict() throws Exception {

        when(userService.updateUser(testUser)).thenThrow(new UserAlreadyExistsException(testUser.getEmail()));

        mockMvc.perform(put(URL + "/{id}", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testRequest)))
                .andExpect(status().isConflict());
    }

    @Test
    void deleteUser_shouldThrowNotFound() throws Exception {
        doThrow(new UserNotFoundException(WRONG_ID)).when(userService).deleteUser(WRONG_ID);

        mockMvc.perform(delete(URL + "/{id}", WRONG_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testRequest)))
                .andExpect(status().isNotFound());
    }

}
