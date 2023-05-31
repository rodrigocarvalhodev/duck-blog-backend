package com.duckdeveloper.blog.configuration.security;

import com.duckdeveloper.blog.configuration.exception.AuthorityException;
import com.duckdeveloper.blog.model.entity.Role;
import com.duckdeveloper.blog.model.entity.User;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class AuthorizationChecker {

    public void verifyIsAnotherUser(User user, User loggedUser) {
        verifyIsAnotherUser(user.getId(), loggedUser);
    }

    public void verifyIsAnotherUser(Long userId, User loggedUser) {
        if (!isSameUserById(loggedUser.getId(), userId) && !this.hasRoleAdmin(loggedUser.getRoles())) {
            throw new AuthorityException("You do not have permission to handle another user");
        }
    }

    private boolean isSameUserById(Long id, Long targetId) {
        return id.equals(targetId);
    }

    private boolean hasRoleAdmin(Collection<Role> roles) {
        return roles.stream()
                .anyMatch(role -> role.getName().equals("ROLE_ADMIN"));
    }
}
