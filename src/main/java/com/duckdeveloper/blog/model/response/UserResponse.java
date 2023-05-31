package com.duckdeveloper.blog.model.response;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserResponse {

    @EqualsAndHashCode.Include
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