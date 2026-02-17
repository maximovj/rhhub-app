package com.github.maximovj.rhhub_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class RhhubAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(RhhubAppApplication.class, args);
	}

}
