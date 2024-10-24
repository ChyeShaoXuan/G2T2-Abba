package com.g4t2project.g4t2project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@RestController
@EnableScheduling
public class AbbaApplication {

	@RequestMapping("/")
	String home() {
		return "Hello World";
	}
	public static void main(String[] args) {
		
		Dotenv dotenv = Dotenv.load();
        System.setProperty("APPLICATION_NAME", dotenv.get("APPLICATION_NAME"));
        System.setProperty("DATASOURCE_URL", dotenv.get("DATASOURCE_URL"));
        System.setProperty("DATASOURCE_USERNAME", dotenv.get("DATASOURCE_USERNAME"));
        System.setProperty("DATASOURCE_PASSWORD", dotenv.get("DATASOURCE_PASSWORD"));
        

		SpringApplication.run(AbbaApplication.class, args);
	}
	

}
