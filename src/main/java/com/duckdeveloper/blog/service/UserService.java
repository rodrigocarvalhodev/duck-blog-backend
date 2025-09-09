package com.duckdeveloper.blog.service;

import com.duckdeveloper.blog.configuration.exception.AuthorityException;
import com.duckdeveloper.blog.configuration.exception.ConstraintAlreadyExistsException;
import com.duckdeveloper.blog.configuration.exception.RoleNotFoundException;
import com.duckdeveloper.blog.configuration.exception.UserNotFoundException;
import com.duckdeveloper.blog.configuration.security.AuthorizationChecker;
import com.duckdeveloper.blog.configuration.security.provider.JWTProvider;
import com.duckdeveloper.blog.model.entity.Role;
import com.duckdeveloper.blog.model.entity.User;
import com.duckdeveloper.blog.model.request.UserRequest;
import com.duckdeveloper.blog.model.response.UserResponse;
import com.duckdeveloper.blog.repository.RoleRepository;
import com.duckdeveloper.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final AuthenticationService authenticationService;
    private final AuthorizationChecker authorizationChecker;

    private final ModelMapper modelMapper;

    private final JWTProvider jwtProvider;

    // Create
    public UserResponse create(UserRequest userRequest) {
        log.info("Roles: {}", userRequest.getRolesId());
        var userToSave = this.mapToEntity(userRequest);
        this.setUpForeignKeys(userToSave);
        this.verifyConstraints(userToSave);
        var userSaved = this.userRepository.save(userToSave);
        return this.mapToResponse(userSaved);
    }

    // Read

    public List<UserResponse> findAll() {
        return userRepository.findAll()
                .stream().map(this::mapToResponse)
                .toList();
    }

    public Optional<UserResponse> findById(Long id) {
        return this.userRepository.findById(id)
                .map(user -> this.modelMapper.map(user, UserResponse.class));
    }

    public Optional<UserResponse> findByUsername(String username) {
        return this.userRepository.findByUsername(username)
                .map(user -> this.modelMapper.map(user, UserResponse.class));
    }

    // Update
    public UserResponse update(Long id, UserRequest userRequest) {
        var userToUpdate = this.userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        var loggedUser = this.authenticationService.getLoggedUser();
        authorizationChecker.verifyIsAnotherUser(userToUpdate, loggedUser);

        var userUpdated = this.mapToEntity(userRequest);
        this.updateData(userToUpdate, userUpdated);
        this.verifyConstraints(userToUpdate);
        this.setUpRoles(userToUpdate, userToUpdate.getRoles());

        var userSaved = this.userRepository.save(userToUpdate);
        return this.mapToResponse(userSaved);
    }

    public UserResponse updateChecked(Long userId) {
        var userToUpdate = this.userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        var loggedUser = this.authenticationService.getLoggedUser();
        authorizationChecker.verifyIsAnotherUser(userId, loggedUser);

        userToUpdate.setChecked(!userToUpdate.isChecked());
        var userSaved = this.userRepository.save(userToUpdate);
        return this.mapToResponse(userSaved);
    }

    public void delete(Long userId) {
        var loggedUser = this.authenticationService.getLoggedUser();
        authorizationChecker.verifyIsAnotherUser(userId, loggedUser);

        this.userRepository.deleteById(userId);
    }

    public void setUpForeignKeys(User user) {
        this.setUpRoles(user, user.getRoles());
    }

    public void setUpRoles(User user, Collection<Role> roles) {
        var newRoles = roles.stream()
                .map(role ->
                        roleRepository.findById(role.getId())
                                .orElseThrow(() -> new RoleNotFoundException(role.getId()))
                )
                .collect(Collectors.toSet());
        user.setRoles(newRoles);
    }

    private void verifyConstraints(User user) {
        verifyUsername(user.getUsername(), user.getId());
        verifyEmail(user.getEmail(), user.getId());
    }

    public void verifyUsername(String username, Long userId) {
        var targetUser = this.userRepository.findByUsernameIgnoreCase(username);
        var constraintUsername = "username";
        if (targetUser != null && (userId == null || !userId.equals(targetUser.getId())))
            throw new ConstraintAlreadyExistsException(constraintUsername, username);
    }

    public void verifyEmail(String email, Long userId) {
        var targetUser = this.userRepository.findByEmailIgnoreCase(email);
        var constraintEmail = "email";
        if (targetUser != null && (userId == null || !userId.equals(targetUser.getId())))
            throw new ConstraintAlreadyExistsException(constraintEmail, email);
    }

    private void updateData(User userToUpdate, User userUpdated) {
        userToUpdate.setFirstName(userUpdated.getFirstName());
        userToUpdate.setLastName(userUpdated.getLastName());
        userToUpdate.setPhoto(userUpdated.getPhoto());
        userToUpdate.setRoles(userUpdated.getRoles());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AuthorityException("User with username %s not found".formatted(username)));


        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                this.jwtProvider.getAuthorities(user.getRoles())
        );
    }

    private User mapToEntity(UserRequest userRequest) {
        return this.modelMapper.map(userRequest, User.class);
    }

    private UserResponse mapToResponse(User user) {
        return this.modelMapper.map(user, UserResponse.class);
    }
}
