package com.duckdeveloper.blog.configuration.exception;

import com.duckdeveloper.blog.configuration.exception.model.Error;
import org.springframework.http.HttpStatus;

public class ConstraintAlreadyExistsException extends BlogException {

    public ConstraintAlreadyExistsException(String constraint, String value) {
        super(new Error("Constraint %s with value %s already exists".formatted(constraint, value), HttpStatus.CONFLICT));
    }
}
