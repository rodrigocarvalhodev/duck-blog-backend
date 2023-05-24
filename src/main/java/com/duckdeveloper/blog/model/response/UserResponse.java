package com.duckdeveloper.blog.model.response;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class UserResponse {

    private Long id;

    private String firstName;
    private String lastName;

    private String email;

    private boolean checked;

    private byte[] photo;

    private LocalDateTime registredTime;

    @JsonManagedReference
    private Set<CommentResponse> comments;

    private Set<RoleResponse> roles = new HashSet<>();
}