package org.example.userserviceapplication.assemblers;

import org.example.userserviceapplication.controller.UserController;
import org.example.userserviceapplication.dto.UserDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class UserModelAssembler implements
        RepresentationModelAssembler<UserDto, EntityModel<UserDto>> {

    @Override
    public EntityModel<UserDto> toModel(UserDto entity) {
        return EntityModel.of(entity, linkTo(methodOn(UserController.class).getUserById(entity.getId())).withSelfRel(),
        linkTo(methodOn(UserController.class).listAllUsers()).withRel("List of all users"));
    }

}
