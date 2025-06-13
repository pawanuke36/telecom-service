package com.pawan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SmsPlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmsPlatformApplication.class, args);
	}

}
