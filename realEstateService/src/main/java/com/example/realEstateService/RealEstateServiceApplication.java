package com.example.realEstateService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;


@SpringBootApplication
@EntityScan("com.example.realEstateService.model")
public class RealEstateServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RealEstateServiceApplication.class, args);
	}

}
