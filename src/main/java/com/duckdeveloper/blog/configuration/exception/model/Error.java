package com.duckdeveloper.blog.configuration.exception.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter @Setter
public class Error {

    private String message;
    private int status;
    private LocalDateTime timestamp;

    public Error(String message, int status) {
        this.message = message;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }

    public Error(String message, HttpStatus status) {
        this(message, status.value());
    }
}
