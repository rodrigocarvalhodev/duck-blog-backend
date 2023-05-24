package com.duckdeveloper.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class DuckBlogBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(DuckBlogBackendApplication.class, args);
    }
}