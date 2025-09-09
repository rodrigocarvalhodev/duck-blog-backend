package com.duckdeveloper.blog.controller;

import com.duckdeveloper.blog.configuration.exception.PostNotFoundException;
import com.duckdeveloper.blog.model.request.PostRequest;
import com.duckdeveloper.blog.model.response.PostResponse;
import com.duckdeveloper.blog.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PreAuthorize("hasAnyRole('ROLE_POST_CREATE_UPDATE_DELETE', 'ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<PostResponse> create(@RequestBody @Valid PostRequest postRequest) {
        var post = postService.create(postRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(post);
    }

    @GetMapping
    public ResponseEntity<Collection<PostResponse>> findAll() {
        var allPosts = postService.findAll();
        return ResponseEntity.ok(allPosts);
    }

    @GetMapping("author/{authorId}")
    public ResponseEntity<Collection<PostResponse>> findAllByAuthorId(@PathVariable Long authorId) {
        var allPostsByAuthor = postService.findAllByAuthorId(authorId);
        return ResponseEntity.ok(allPostsByAuthor);
    }

    @PreAuthorize("hasAnyRole('ROLE_POST_CREATE_UPDATE_DELETE', 'ROLE_ADMIN')")
    @PutMapping("{postId}")
    public ResponseEntity<PostResponse> update(@PathVariable Long postId, @RequestBody @Valid PostRequest postRequest) {
        var postUpdated = postService.update(postId, postRequest);
        return ResponseEntity.ok(postUpdated);
    }

    @GetMapping("{postId}")
    public ResponseEntity<PostResponse> findById(@PathVariable Long postId) {
        return postService.findById(postId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new PostNotFoundException(postId));
    }

    @PreAuthorize("hasAnyRole('ROLE_POST_CREATE_UPDATE_DELETE', 'ROLE_ADMIN')")
    @DeleteMapping("{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long postId) {
        postService.delete(postId);
    }
}
