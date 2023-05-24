package com.duckdeveloper.blog.repository;

import com.duckdeveloper.blog.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    User findByUsernameIgnoreCase(String username);

    User findByEmailIgnoreCase(String email);

}
