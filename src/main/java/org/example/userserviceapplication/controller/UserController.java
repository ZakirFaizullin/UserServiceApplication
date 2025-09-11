package org.example.userserviceapplication.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.userserviceapplication.dto.UserCreateRequest;
import org.example.userserviceapplication.dto.UserDto;
import org.example.userserviceapplication.model.StringValue;
import org.example.userserviceapplication.model.User;
import org.example.userserviceapplication.services.DataSenderKafka;
import org.example.userserviceapplication.services.UserService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Пользователи", description = "Взаимодействие с пользователями")
public class UserController {

    private final UserService userService;
    private final DataSenderKafka dataSenderKafka;
    private final RepresentationModelAssembler<UserDto, EntityModel<UserDto>> assembler;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Регистрация пользователя", description = "Позволяет зарегистрировать пользователя")
    public EntityModel<UserDto> createUser(@RequestBody UserCreateRequest request) {
        UserDto result = userService.createUser(request);
        dataSenderKafka.sendMessage(new StringValue("created", request.getEmail()));
        return assembler.toModel(result);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить пользователя", description = "Позволяет получить пользователя")
    public EntityModel<UserDto> getUserById(
            @PathVariable @Parameter(description = "Идентификатор пользователя", required = true) Long id) {
        return assembler.toModel(userService.findUserById(id));
    }

    @GetMapping
    @Operation(summary = "Получить всех пользователей", description = "Позволяет получить всех пользователей")
    public CollectionModel<EntityModel<UserDto>> listAllUsers() {
        List<EntityModel<UserDto>> users = userService.findAllUsers().stream()
                .map(assembler::toModel)
                .toList();
        return CollectionModel.of(users,
                linkTo(methodOn(UserController.class).listAllUsers()).withSelfRel());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновление пользователя", description = "Позволяет обновить данные пользователя")
    public EntityModel<UserDto> updateUser(
            @PathVariable @Parameter(description = "Идентификатор пользователя", required = true) Long id,
            @RequestBody User updatedUser) {
        updatedUser.setId(id);
        return assembler.toModel(userService.updateUser(updatedUser));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление пользователя", description = "Позволяет удалить пользователя")
    public ResponseEntity<Void> deleteUser(
            @PathVariable @Parameter(description = "Идентификатор пользователя", required = true) Long id) {
        UserDto foundUser = userService.findUserById(id);
        if (foundUser != null)
            dataSenderKafka.sendMessage(new StringValue("deleted", foundUser.getEmail()));
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}
