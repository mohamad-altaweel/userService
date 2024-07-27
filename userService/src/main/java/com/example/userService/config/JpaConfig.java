package com.example.userService.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EntityScan(basePackages = "com.example.userservice.model") // Replace with your entity package
public class JpaConfig {
}
