package com.duckdeveloper.blog.repository;

import com.duckdeveloper.blog.model.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Collection<Post> findAllByAuthorId(Long authorId);
}
