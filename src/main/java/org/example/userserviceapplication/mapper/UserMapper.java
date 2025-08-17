package org.example.userserviceapplication.mapper;

import org.example.userserviceapplication.dto.UserCreateRequest;
import org.example.userserviceapplication.dto.UserDto;
import org.example.userserviceapplication.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User requestToEntity(UserCreateRequest request);

    UserDto entityToDto(User entity);
}
