package com.duckdeveloper.blog.configuration.exception;

import com.duckdeveloper.blog.configuration.exception.model.Error;
import org.springframework.http.HttpStatus;

public class CommentNotFoundException extends BlogException {

    public CommentNotFoundException(Long id) {
        super(new Error("Comment with id %d not found".formatted(id), HttpStatus.NOT_FOUND, 4));
    }
}