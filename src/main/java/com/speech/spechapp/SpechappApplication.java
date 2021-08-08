package com.speech.spechapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class SpechappApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpechappApplication.class, args);
	}

}
