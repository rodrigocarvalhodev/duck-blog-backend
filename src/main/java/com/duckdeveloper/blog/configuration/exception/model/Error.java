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
    private int errorId;
    private LocalDateTime timestamp;

    public Error(String message, int status, int errorId) {
        this.message = message;
        this.status = status;
        this.errorId = errorId;
        this.timestamp = LocalDateTime.now();
    }

    public Error(String message, HttpStatus status, int errorId) {
        this(message, status.value(), errorId);
    }
}
