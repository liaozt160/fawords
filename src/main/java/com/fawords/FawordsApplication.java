package com.fawords;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@org.springframework.data.jpa.repository.config.EnableJpaRepositories(basePackages = "com.example.demo.repository")
@org.springframework.boot.autoconfigure.domain.EntityScan(basePackages = "com.example.demo.entity")
public class FawordsApplication {

	public static void main(String[] args) {
		SpringApplication.run(FawordsApplication.class, args);
	}

}
