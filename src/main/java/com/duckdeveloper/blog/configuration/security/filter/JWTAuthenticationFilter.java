package com.duckdeveloper.blog.configuration.security.filter;

import com.duckdeveloper.blog.configuration.security.provider.JWTProvider;
import com.duckdeveloper.blog.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final UserService userService;
    private final JWTProvider jwtProvider;

    @Override
    @SneakyThrows
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        var tokenOptional = Optional.ofNullable(request.getHeader("Authorization"))
                .map(tokenMap -> tokenMap.replace("Bearer ", ""));

        if (tokenOptional.isPresent()) {
            var token = tokenOptional.get();

            var username = jwtProvider.getUsernameFromToken(token);
            var userDetails = this.userService.loadUserByUsername(username);
            if (jwtProvider.validateToken(token)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = jwtProvider.getAuthenticationToken(token, userDetails);
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
