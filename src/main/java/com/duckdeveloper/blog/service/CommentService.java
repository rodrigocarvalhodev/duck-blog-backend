package com.duckdeveloper.blog.service;

import com.duckdeveloper.blog.configuration.exception.CommentNotFoundException;
import com.duckdeveloper.blog.configuration.exception.PostNotFoundException;
import com.duckdeveloper.blog.configuration.security.AuthorizationChecker;
import com.duckdeveloper.blog.model.entity.Comment;
import com.duckdeveloper.blog.model.entity.Post;
import com.duckdeveloper.blog.model.entity.User;
import com.duckdeveloper.blog.model.request.CommentRequest;
import com.duckdeveloper.blog.model.response.CommentResponse;
import com.duckdeveloper.blog.repository.CommentRepository;
import com.duckdeveloper.blog.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    private final AuthenticationService authenticationService;
    private final AuthorizationChecker authorizationChecker;

    public CommentResponse create(CommentRequest commentRequest) {
        var comment = this.mapToEntity(commentRequest);
        this.verifyForeignKeys(comment);
        this.setUpAuthor(comment);
        var commentSaved = this.commentRepository.save(comment);
        return this.mapToResponse(commentSaved);
    }

    public Collection<CommentResponse> findAllByAuthorId(Long authorId) {
        return this.commentRepository.findAllByAuthorId(authorId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public Collection<CommentResponse> findAllByPostId(Long postId) {
        return this.commentRepository.findAllByPostId(postId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public CommentResponse update(Long commentId, CommentRequest commentRequest) {
        var commentToUpdate = this.commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));

        var loggedUser = this.authenticationService.getLoggedUser();
        authorizationChecker.verifyIsAnotherUser(commentToUpdate.getAuthor(), loggedUser);
        verifyIsAnotherPost(commentToUpdate.getPost(), commentRequest.getPostId());
        updateData(commentToUpdate, commentRequest);
        var commentSaved = this.commentRepository.save(commentToUpdate);
        return this.mapToResponse(commentSaved);
    }

    private void verifyForeignKeys(Comment comment) {
        this.verifyPost(comment, comment.getPost());
    }

    private void verifyPost(Comment comment, Post post) {
        var postManaged = this.postRepository.findById(post.getId())
                .orElseThrow(() -> new PostNotFoundException(post.getId()));
        comment.setPost(postManaged);
    }

    private void verifyIsAnotherPost(Post post, Long postId) {

    }

    public void setUpAuthor(Comment comment) {
        var loggedUser = this.authenticationService.getLoggedUser();
        comment.setAuthor(loggedUser);
    }

    private void updateData(Comment commentToUpdate, CommentRequest commentRequest) {
        commentToUpdate.setText(commentRequest.getText());
    }

    private Comment mapToEntity(CommentRequest commentRequest) {
        return this.modelMapper.map(commentRequest, Comment.class);
    }

    private CommentResponse mapToResponse(Comment comment) {
        return this.modelMapper.map(comment, CommentResponse.class);
    }
}
