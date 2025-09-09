package com.duckdeveloper.blog.controller;

import com.duckdeveloper.blog.configuration.exception.AuthorityException;
import com.duckdeveloper.blog.model.request.LoginRequest;
import com.duckdeveloper.blog.model.request.UserRequest;
import com.duckdeveloper.blog.model.response.LoginResponse;
import com.duckdeveloper.blog.model.response.UserResponse;
import com.duckdeveloper.blog.service.AuthenticationService;
import com.duckdeveloper.blog.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping
    public ResponseEntity<UserResponse> create(@RequestBody @Valid UserRequest userRequest) {
        var user = userService.create(userRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(user);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_USER_VIEW_ALL', 'ROLE_ADMIN')")
    public ResponseEntity<Collection<UserResponse>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER_VIEW', 'ROLE_ADMIN')")
    public ResponseEntity<UserResponse> findById(@PathVariable Long id) {
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new AuthorityException("User with id '%d' not found".formatted(id)));
    }

    @GetMapping("username/{username}")
    @PreAuthorize("hasAnyRole('ROLE_USER_VIEW', 'ROLE_ADMIN')")
    public ResponseEntity<UserResponse> findByUsername(@PathVariable String username) {
        return userService.findByUsername(username)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new AuthorityException("User with username '%s' not found".formatted(username)));
    }

    @PutMapping("{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER_UPDATE', 'ROLE_ADMIN')")
    public ResponseEntity<UserResponse> update(@PathVariable Long id, @RequestBody @Valid UserRequest userRequest) {
        return ResponseEntity.ok(this.userService.update(id, userRequest));
    }

    @PatchMapping("{id}/checked")
    @PreAuthorize("hasAnyRole('ROLE_USER_UPDATE', 'ROLE_ADMIN')")
    public ResponseEntity<UserResponse> updateChecked(@PathVariable Long id) {
        return ResponseEntity.ok(this.userService.updateChecked(id));
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER_DELETE', 'ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        this.userService.delete(id);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        return this.authenticationService.login(loginRequest)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new AuthorityException("Username or Password invalid."));
    }
}