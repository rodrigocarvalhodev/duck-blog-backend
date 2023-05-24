package com.duckdeveloper.blog.service;

import com.duckdeveloper.blog.model.response.PostResponse;
import com.duckdeveloper.blog.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    public Collection<PostResponse> findAll() {
        return this.postRepository.findAll().stream()
                .map(post -> this.modelMapper.map(post, PostResponse.class))
                .toList();
    }
}
