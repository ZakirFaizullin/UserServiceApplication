package org.example.userserviceapplication.controller;

import lombok.RequiredArgsConstructor;
import org.example.userserviceapplication.dto.UserCreateRequest;
import org.example.userserviceapplication.dto.UserDto;
import org.example.userserviceapplication.mapper.UserMapper;
import org.example.userserviceapplication.model.User;
import org.example.userserviceapplication.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper mapper;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@RequestBody UserCreateRequest request) {
        return mapper.entityToDto(userService.createUser(request));
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        return mapper.entityToDto(userService.findUserById(id));
    }

    @GetMapping
    public List<UserDto> listAllUsers() {
        return userService.findAllUsers().stream()
                .map(mapper::entityToDto)
                .collect(Collectors.toList());
    }

    @PutMapping("/{id}")
    public UserDto updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        updatedUser.setId(id);
        return mapper.entityToDto(userService.updateUser(updatedUser));
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

}
