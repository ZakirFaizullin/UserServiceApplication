package org.example.userserviceapplication.controller;

import lombok.RequiredArgsConstructor;
import org.example.userserviceapplication.dto.UserCreateRequest;
import org.example.userserviceapplication.dto.UserDto;
import org.example.userserviceapplication.model.StringValue;
import org.example.userserviceapplication.model.User;
import org.example.userserviceapplication.services.DataSenderKafka;
import org.example.userserviceapplication.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final DataSenderKafka dataSenderKafka;
    

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@RequestBody UserCreateRequest request) {
        UserDto result = userService.createUser(request);
        dataSenderKafka.sendMessage(new StringValue("created", request.getEmail()));
        return result;
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        return userService.findUserById(id);
    }

    @GetMapping
    public List<UserDto> listAllUsers() {
        return userService.findAllUsers();
    }

    @PutMapping("/{id}")
    public UserDto updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        updatedUser.setId(id);
        return userService.updateUser(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        UserDto foundUser = userService.findUserById(id);
        if (foundUser != null)
            dataSenderKafka.sendMessage(new StringValue("deleted", foundUser.getEmail()));
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}
