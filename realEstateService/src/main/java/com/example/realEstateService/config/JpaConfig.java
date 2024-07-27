package com.example.realEstateService.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EntityScan(basePackages = "com.example.realEstateService.model") // Replace with your entity package
public class JpaConfig {
}
