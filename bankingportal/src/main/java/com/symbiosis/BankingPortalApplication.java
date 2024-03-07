package com.symbiosis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan({ "com.symbiosis", "com.dao", "com,model", "com.service", "com.controller" })
@EntityScan("com.model")
@EnableJpaRepositories(basePackages = "com.dao")
public class BankingPortalApplication {
	public static void main(String[] args) {
		SpringApplication.run(BankingPortalApplication.class, args);
	}

}

