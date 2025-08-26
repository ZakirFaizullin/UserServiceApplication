package org.example.userserviceapplication.exceptions;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String email) {
        super("User with an email address " + email + " already exists");
    }
}
