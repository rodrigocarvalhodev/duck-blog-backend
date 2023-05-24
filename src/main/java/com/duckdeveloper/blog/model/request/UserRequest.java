package com.duckdeveloper.blog.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class UserRequest {

    @NotEmpty(message = "firstName can't be empty")
    private String firstName;

    @NotEmpty(message = "lastName can't be empty")
    private String lastName;

    @NotEmpty(message = "username can't be empty")
    private String username;

    @NotEmpty(message = "email can't be empty")
    @Email(message = "this email is not valid")
    private String email;

    @NotEmpty(message = "password can't be empty")
    @Size(min = 8, max = 256, message = "password must be at least 8 characters and a maximum of 256")
    private String password;

    private byte[] photo;

    private Set<Long> rolesId = new HashSet<>();

}
