package com.duckdeveloper.blog.service;

import com.duckdeveloper.blog.configuration.exception.AuthorityException;
import com.duckdeveloper.blog.configuration.exception.UserNotFoundException;
import com.duckdeveloper.blog.configuration.security.provider.JWTProvider;
import com.duckdeveloper.blog.model.entity.User;
import com.duckdeveloper.blog.model.request.LoginRequest;
import com.duckdeveloper.blog.model.response.LoginResponse;
import com.duckdeveloper.blog.repository.UserRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

@Service
@Slf4j
public class AuthenticationService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final JWTProvider jwtProvider;

    @Lazy
    public AuthenticationService(UserService userService, UserRepository userRepository, JWTProvider jwtProvider) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
    }

    @SneakyThrows
    public Optional<LoginResponse> login(LoginRequest loginRequest) {
        var authentication = this.generateAuthenticationAndSetupContext(loginRequest);

        var token = this.jwtProvider.generateToken(authentication);

        return this.userService.findByUsername(loginRequest.getUsername())
                .map(userResponse -> new LoginResponse(userResponse, token));
    }

    public User getLoggedUser() {
        var token = getBearerTokenHeader().replace("Bearer ", "");
        if (token.isEmpty()) {
            throw new AuthorityException("User Logged Not Found!");
        }
        var username = this.jwtProvider.getUsernameFromToken(token);
        return this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    public static String getBearerTokenHeader() {
        var requestAttributes =  (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null)
            return "";

        return requestAttributes.getRequest().getHeader("Authorization");
    }

    private Authentication generateAuthenticationAndSetupContext(LoginRequest loginRequest) {
        var authentication = this.jwtProvider.generateAuthentication(loginRequest.getUsername(), loginRequest.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }
}
