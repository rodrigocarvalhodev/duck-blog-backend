package com.duckdeveloper.blog.model.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class PostRequest {

    @Max(value = 256, message = "title can only be 200 characters long ")
    private String title;

    @NotEmpty(message = "text can't be empty")
    private String text;

}