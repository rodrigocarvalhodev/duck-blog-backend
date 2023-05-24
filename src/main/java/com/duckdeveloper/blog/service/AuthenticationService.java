package com.duckdeveloper.blog.service;

import com.duckdeveloper.blog.configuration.security.provider.JWTProvider;
import com.duckdeveloper.blog.model.request.LoginRequest;
import com.duckdeveloper.blog.model.response.LoginResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;
    private final JWTProvider jwtProvider;

    @SneakyThrows
    public Optional<LoginResponse> login(LoginRequest loginRequest) {
        var authentication = this.generateAuthenticationAndSetupContext(loginRequest);

        var token = this.jwtProvider.generateToken(authentication);

        return this.userService.findByUsername(loginRequest.getUsername())
                .map((userResponse) -> new LoginResponse(userResponse, token));
    }

    private Authentication generateAuthenticationAndSetupContext(LoginRequest loginRequest) {
        var authentication = this.jwtProvider.generateAuthentication(loginRequest.getUsername(), loginRequest.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }
}
