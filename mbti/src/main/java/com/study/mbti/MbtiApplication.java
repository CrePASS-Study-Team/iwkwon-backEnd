package com.study.mbti;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MbtiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MbtiApplication.class, args);
	}

}
