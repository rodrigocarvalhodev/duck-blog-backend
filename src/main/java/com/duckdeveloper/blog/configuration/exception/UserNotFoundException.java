package com.duckdeveloper.blog.configuration.exception;

import com.duckdeveloper.blog.configuration.exception.model.Error;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends BlogException {

    private static final int ERROR_ID = 5;

    public UserNotFoundException(Long id) {
        super(new Error("User with id %d not found".formatted(id), HttpStatus.NOT_FOUND, ERROR_ID));
    }

    public UserNotFoundException(String username) {
        super(new Error("User with username %s not found".formatted(username), HttpStatus.NOT_FOUND, ERROR_ID));
    }
}
