package com.duckdeveloper.blog.configuration.exception;

import com.duckdeveloper.blog.configuration.exception.model.Error;
import org.springframework.http.HttpStatus;

public class PostNotFoundException extends BlogException {

    public PostNotFoundException(Long id) {
        super(new Error("Post with id %d not found".formatted(id), HttpStatus.NOT_FOUND, 4));
    }
}
