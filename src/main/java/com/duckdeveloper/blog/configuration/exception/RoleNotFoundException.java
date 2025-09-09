package com.duckdeveloper.blog.configuration.exception;

import com.duckdeveloper.blog.configuration.exception.model.Error;
import org.springframework.http.HttpStatus;

public class RoleNotFoundException extends BlogException {

    public RoleNotFoundException(Long id) {
        super(new Error("Role with ID %d not found".formatted(id), HttpStatus.NOT_FOUND, 3));
    }
}
