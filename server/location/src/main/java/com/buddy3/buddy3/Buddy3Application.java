package com.buddy3.buddy3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class Buddy3Application {

	public static void main(String[] args) {
		SpringApplication.run(Buddy3Application.class, args);
	}

}
