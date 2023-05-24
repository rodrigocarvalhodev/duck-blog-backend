package com.duckdeveloper.blog.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class LoginResponse {

    private UserResponse userResponse;
    private String token;

}
