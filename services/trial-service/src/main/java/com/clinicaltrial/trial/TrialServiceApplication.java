package com.clinicaltrial.trial;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class TrialServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrialServiceApplication.class, args);
	}

}
