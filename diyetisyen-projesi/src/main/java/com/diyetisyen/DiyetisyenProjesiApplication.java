package com.diyetisyen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.diyetisyen")
@EntityScan(basePackages = "com.diyetisyen")
public class DiyetisyenProjesiApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiyetisyenProjesiApplication.class, args);
	}
}