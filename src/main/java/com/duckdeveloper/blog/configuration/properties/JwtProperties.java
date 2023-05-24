package com.duckdeveloper.blog.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "jwt")
@Getter @Setter
public class JwtProperties {

    private String header;
    private Long tokenValidity;
    private String tokenPrefix;
    private String signingKey;
    private String authoritiesKey;

}
