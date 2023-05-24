package com.duckdeveloper.blog.configuration.exception;

import com.duckdeveloper.blog.configuration.exception.model.Error;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AuthorityException extends BlogException {

    public AuthorityException(String reason) {
        super(new Error(reason, HttpStatus.FORBIDDEN));
    }
}
