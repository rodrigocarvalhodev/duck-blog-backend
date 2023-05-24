package com.duckdeveloper.blog.configuration.exception;

import com.duckdeveloper.blog.configuration.exception.model.Error;

public abstract class BlogException extends RuntimeException {

    protected Error error;

    protected BlogException(Error error) {
        super(error.getMessage());
        this.error = error;
    }

    public Error getError() {
        return error;
    }
}
