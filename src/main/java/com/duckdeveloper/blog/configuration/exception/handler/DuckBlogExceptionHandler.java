package com.duckdeveloper.blog.configuration.exception.handler;

import com.duckdeveloper.blog.configuration.exception.BlogException;
import com.duckdeveloper.blog.configuration.exception.model.Error;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class DuckBlogExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BlogException.class)
    public ResponseEntity<Error> handleBlogException(BlogException exception) {
        var error = exception.getError();
        return ResponseEntity
                .status(error.getStatus())
                .body(error);
    }

    @Override
    protected ResponseEntity<Object> handleErrorResponseException(ErrorResponseException exception, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        var error = new Error(exception.getMessage(), exception.getStatusCode().value(), 6);
        return ResponseEntity
                .status(exception.getStatusCode())
                .body(error);
    }
}
