package com.duckdeveloper.blog.controller;

import com.duckdeveloper.blog.model.response.PostResponse;
import com.duckdeveloper.blog.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping
    public ResponseEntity<Collection<PostResponse>> findAll() {
        var allPosts = postService.findAll();
        return ResponseEntity.ok(allPosts);
    }
}
