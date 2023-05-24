package com.duckdeveloper.blog.service;

import com.duckdeveloper.blog.configuration.exception.ConstraintAlreadyExistsException;
import com.duckdeveloper.blog.model.entity.Role;
import com.duckdeveloper.blog.model.entity.User;
import com.duckdeveloper.blog.repository.RoleRepository;
import com.duckdeveloper.blog.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Test
    void whenSetUpRolesReturnsCorrectValues() {
        when(roleRepository.findById(1L))
                .thenReturn(Optional.of(new Role(1L, "ROLE_TEST_1")));
        when(roleRepository.findById(2L))
                .thenReturn(Optional.of(new Role(2L, "ROLE_TEST_2")));

        var roles = Set.of(
                Role.builder().id(1L).build(),
                Role.builder().id(2L).build()
        );

        var user = User.builder()
                .roles(roles)
                .build();

        userService.setUpRoles(user, roles);

        var userRoles = user.getRoles();

        userRoles.forEach(role ->
                assertEquals(role.getName(),
                        String.format("ROLE_TEST_%d", role.getId())
                )
        );
    }

    @Test
    void whenVerifyUsernameThrowsConstraintAlreadyExistsException() {
        when(userRepository.findByUsernameIgnoreCase("duckdeveloper"))
                .thenReturn(User.builder().username("duckdeveloper").build());
        when(userRepository.findByUsernameIgnoreCase("DUCKDEVELOPER"))
                .thenReturn(User.builder().username("DUCKDEVELOPER").build());

        assertThrows(ConstraintAlreadyExistsException.class, () -> userService.verifyUsername("duckdeveloper", null));
        assertThrows(ConstraintAlreadyExistsException.class, () -> userService.verifyUsername("DUCKDEVELOPER", null));
    }

    @Test
    void whenVerifyEmailThrowsConstraintAlreadyExistsException() {
        when(userRepository.findByEmailIgnoreCase("rodrigocarvalhodev@gmail.com"))
                .thenReturn(User.builder().email("rodrigocarvalhodev@gmail.com").build());
        when(userRepository.findByEmailIgnoreCase("RODRIGOCARVALHODEV@gmail.com"))
                .thenReturn(User.builder().email("RODRIGOCARVALHODEV@gmail.com").build());

        assertThrows(ConstraintAlreadyExistsException.class, () -> userService.verifyEmail("rodrigocarvalhodev@gmail.com", null));
        assertThrows(ConstraintAlreadyExistsException.class, () -> userService.verifyEmail("RODRIGOCARVALHODEV@gmail.com", null));
    }
}