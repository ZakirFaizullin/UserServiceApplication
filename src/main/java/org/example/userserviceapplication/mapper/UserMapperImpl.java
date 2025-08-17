package org.example.userserviceapplication.mapper;

import org.example.userserviceapplication.dto.UserCreateRequest;
import org.example.userserviceapplication.dto.UserDto;
import org.example.userserviceapplication.model.User;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public class UserMapperImpl implements UserMapper {
    @Override
    public User requestToEntity(UserCreateRequest request) {
        return new User(null,
                request.getName(),
                request.getAge(),
                request.getEmail(),
                null);
    }
    @Override
    public UserDto entityToDto(User entity) {
        return new UserDto(entity.getId(),
                entity.getName(),
                entity.getAge(),
                entity.getEmail(),
                entity.getCreatedAt());
    }
}
