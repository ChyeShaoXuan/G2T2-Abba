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
        // Load environment variables and set as system properties
        Dotenv dotenv = Dotenv.configure().load();

        setSystemProperty("APPLICATION_NAME", dotenv.get("APPLICATION_NAME"));
        setSystemProperty("DATASOURCE_URL", dotenv.get("DATASOURCE_URL"));
        setSystemProperty("DATASOURCE_USERNAME", dotenv.get("DATASOURCE_USERNAME"));
        setSystemProperty("DATASOURCE_PASSWORD", dotenv.get("DATASOURCE_PASSWORD"), true); // Allow empty value
        setSystemProperty("SMTP_HOST", dotenv.get("SMTP_HOST"));
        setSystemProperty("SMTP_PORT", dotenv.get("SMTP_PORT"));
        setSystemProperty("SMTP_USERNAME", dotenv.get("SMTP_USERNAME"));
        setSystemProperty("SMTP_PASSWORD", dotenv.get("SMTP_PASSWORD"));
        setSystemProperty("APP_BASE_URL", dotenv.get("APP_BASE_URL"));
        setSystemProperty("GOOGLE_MAPS_API_KEY", dotenv.get("GOOGLE_MAPS_API_KEY"));

        // Print to confirm the properties are set
        System.out.println("Loaded APPLICATION_NAME: " + System.getProperty("APPLICATION_NAME"));

        SpringApplication.run(AbbaApplication.class, args);
    }

    private static void setSystemProperty(String key, String value) {
        setSystemProperty(key, value, false);
    }

    private static void setSystemProperty(String key, String value, boolean allowEmpty) {
        if (value != null && (allowEmpty || !value.isEmpty())) {
            System.setProperty(key, value);
        } else {
            throw new IllegalArgumentException("Environment variable " + key + " is not set.");
        }
    }
}
