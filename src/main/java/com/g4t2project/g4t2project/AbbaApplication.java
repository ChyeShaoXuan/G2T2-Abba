package com.g4t2project.g4t2project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@SpringBootApplication
@RestController
public class AbbaApplication {

	@RequestMapping("/")
	String home() {
		return "Hello World";
	}
	public static void main(String[] args) {
		SpringApplication.run(AbbaApplication.class, args);
	}
	

}
