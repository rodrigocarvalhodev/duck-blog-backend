package com.duckdeveloper.blog.configuration.security.provider;

import com.duckdeveloper.blog.configuration.properties.JwtProperties;
import com.duckdeveloper.blog.model.entity.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JWTProvider {

    private final JwtProperties jwtProperties;
    private final AuthenticationManager authenticationManager;

    @Lazy
    public JWTProvider(JwtProperties jwtProperties, AuthenticationManager authenticationManager) {
        this.jwtProperties = jwtProperties;
        this.authenticationManager = authenticationManager;
    }

    public String generateToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(jwtProperties.getAuthoritiesKey(), authorities)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getTokenValidity()))
                .signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encodeToString(jwtProperties.getSigningKey().getBytes()))
                .compact();
    }

    public Authentication generateAuthentication(String username, String password) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username,
                        password
                )
        );
    }

    public Collection<? extends GrantedAuthority> getAuthorities(Collection<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .toList();
    }

    public UsernamePasswordAuthenticationToken getAuthenticationToken(String token, UserDetails userDetails) {
        var jwtParser = Jwts.parser().setSigningKey(Base64.getEncoder().encodeToString(jwtProperties.getSigningKey().getBytes()));

        var claimsJws = jwtParser.parseClaimsJws(token);
        var claims = claimsJws.getBody();

        var authorities = Arrays.stream(claims.get(jwtProperties.getAuthoritiesKey())
                        .toString().split(",")
                )
                .map(SimpleGrantedAuthority::new)
                .toList();

        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
    }

    public boolean validateToken(String token) {
        return !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(Base64.getEncoder().encodeToString(jwtProperties.getSigningKey().getBytes()))
                .parseClaimsJws(token)
                .getBody();
    }
}
