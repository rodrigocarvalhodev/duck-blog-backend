package com.duckdeveloper.blog.service;

import com.duckdeveloper.blog.configuration.exception.PostNotFoundException;
import com.duckdeveloper.blog.configuration.security.AuthorizationChecker;
import com.duckdeveloper.blog.model.entity.Post;
import com.duckdeveloper.blog.model.request.PostRequest;
import com.duckdeveloper.blog.model.response.PostResponse;
import com.duckdeveloper.blog.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    private final AuthenticationService authenticationService;
    private final AuthorizationChecker authorizationChecker;

    // Create
    public PostResponse create(PostRequest postRequest) {
        var postToSave = this.mapToEntity(postRequest);
        this.setUpForeignKeys(postToSave);
        var postSaved = this.postRepository.save(postToSave);
        return this.mapToResponse(postSaved);
    }

    // Read
    public Collection<PostResponse> findAll() {
        return this.postRepository.findAll().stream()
                .map(post -> this.modelMapper.map(post, PostResponse.class))
                .toList();
    }

    public Collection<PostResponse> findAllByAuthorId(Long authorId) {
        return this.postRepository.findAllByAuthorId(authorId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toSet());
    }

    public Optional<PostResponse> findById(Long postId) {
        return this.postRepository.findById(postId)
                .map(this::mapToResponse);
    }

    // Update
    public PostResponse update(Long postId, PostRequest postRequest) {
        var postToUpdate = this.postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        var loggedUser = this.authenticationService.getLoggedUser();
        authorizationChecker.verifyIsAnotherUser(postToUpdate.getAuthor(), loggedUser);
        updateData(postToUpdate, postRequest);
        var postUpdated = this.postRepository.save(postToUpdate);
        return this.mapToResponse(postUpdated);
    }

    // Delete
    public void delete(Long postId) {
        var post = this.postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        var loggedUser = this.authenticationService.getLoggedUser();
        authorizationChecker.verifyIsAnotherUser(post.getAuthor(), loggedUser);
        this.postRepository.deleteById(postId);
    }

    public void setUpForeignKeys(Post post) {
        setUpAuthor(post);
    }

    public void setUpAuthor(Post post) {
        var loggedUser = this.authenticationService.getLoggedUser();
        post.setAuthor(loggedUser);
    }

    private void updateData(Post postToUpdate, PostRequest postRequest) {
        postToUpdate.setTitle(postRequest.getTitle());
        postToUpdate.setText(postRequest.getText());
    }

    private Post mapToEntity(PostRequest postRequest) {
        return this.modelMapper.map(postRequest, Post.class);
    }

    private PostResponse mapToResponse(Post post) {
        return this.modelMapper.map(post, PostResponse.class);
    }
}
