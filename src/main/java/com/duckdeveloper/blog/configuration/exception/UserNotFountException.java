package com.duckdeveloper.blog.configuration.exception;

import com.duckdeveloper.blog.configuration.exception.model.Error;
import org.springframework.http.HttpStatus;

public class UserNotFountException extends BlogException {

    public UserNotFountException(Long id) {
        super(new Error("User with id %d not found".formatted(id), HttpStatus.NOT_FOUND));
    }
}
