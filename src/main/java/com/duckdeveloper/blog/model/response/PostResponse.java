package com.duckdeveloper.blog.model.response;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class PostResponse {

    private Long id;

    private String text;

    private LocalDateTime creationTime;

    private LocalDateTime updatedTime;

    private UserResponse createdBy;

    @JsonManagedReference
    private List<CommentResponse> comments;
}
