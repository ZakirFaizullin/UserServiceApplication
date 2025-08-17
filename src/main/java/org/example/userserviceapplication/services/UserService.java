package org.example.userserviceapplication.services;

import lombok.RequiredArgsConstructor;
import org.example.userserviceapplication.dto.UserCreateRequest;
import org.example.userserviceapplication.mapper.UserMapper;
import org.example.userserviceapplication.model.User;
import org.example.userserviceapplication.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper mapper;


    public User createUser(UserCreateRequest request) {
        var newUser = mapper.requestToEntity(request);
        newUser.setCreatedAt(LocalDateTime.now());
        return userRepository.save(newUser);
    }

    public User findUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User updateUser(User updatedUser) {
        return userRepository.save(updatedUser);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

}
