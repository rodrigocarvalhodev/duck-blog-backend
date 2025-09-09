package com.duckdeveloper.blog.model.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class CommentRequest {

    @NotEmpty(message = "text can't be empty")
    private String text;

    @NotNull(message = "postId can't be null")
    private Long postId;

}
