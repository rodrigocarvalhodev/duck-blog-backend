package com.duckdeveloper.blog.model.response;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class CommentResponse {

    private Long id;

    private String text;

    @JsonBackReference
    private UserResponse author;

    @JsonBackReference
    private PostResponse post;
}
