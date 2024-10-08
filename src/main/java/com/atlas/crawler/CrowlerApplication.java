package com.atlas.crawler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CrowlerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrowlerApplication.class, args);
	}

}
