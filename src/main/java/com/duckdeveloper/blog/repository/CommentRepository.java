package com.duckdeveloper.blog.repository;

import com.duckdeveloper.blog.model.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Collection<Comment> findAllByAuthorIdOrderByCreatedTimeDesc(Long authorId);

    Collection<Comment> findAllByPostIdOrderByCreatedTimeDesc(Long postId);

    default Collection<Comment> findAllByAuthorId(Long authorId) {
        return this.findAllByAuthorIdOrderByCreatedTimeDesc(authorId);
    }

    default Collection<Comment> findAllByPostId(Long postId) {
        return this.findAllByPostIdOrderByCreatedTimeDesc(postId);
    }

}
