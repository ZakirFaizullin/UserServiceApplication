package org.example.userserviceapplication.services;

import lombok.RequiredArgsConstructor;
import org.example.userserviceapplication.dto.UserCreateRequest;
import org.example.userserviceapplication.dto.UserDto;
import org.example.userserviceapplication.exceptions.UserAlreadyExistsException;
import org.example.userserviceapplication.exceptions.UserNotFoundException;
import org.example.userserviceapplication.mapper.UserMapper;
import org.example.userserviceapplication.model.User;
import org.example.userserviceapplication.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper mapper;


    public UserDto createUser(UserCreateRequest request) {
        if (userRepository.existsByEmail(request.getEmail()))
            throw new UserAlreadyExistsException(request.getEmail());
        User newUser = mapper.requestToEntity(request);
        return mapper.entityToDto(userRepository.save(newUser));
    }

    public UserDto findUserById(Long id) {
        return mapper.entityToDto(userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException(id)));
    }

    public List<UserDto> findAllUsers() {
        return userRepository.findAll().stream()
                .map(mapper::entityToDto)
                .collect(Collectors.toList());
    }

    public UserDto updateUser(User updatedUser) {
        Long id = updatedUser.getId();
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        if (!user.getEmail().equals(updatedUser.getEmail()) &&
                userRepository.existsByEmail(updatedUser.getEmail())) {
            throw new UserAlreadyExistsException(updatedUser.getEmail());
        }

        return mapper.entityToDto(userRepository.save(updatedUser));
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id))
            throw new UserNotFoundException(id);
        userRepository.deleteById(id);
    }

}
