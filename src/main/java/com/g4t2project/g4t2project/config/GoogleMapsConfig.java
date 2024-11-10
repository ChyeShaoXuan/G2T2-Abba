package com.g4t2project.g4t2project.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GoogleMapsConfig {

    @Value("${google.maps.api.key}")
    private String apiKey;

    public String getApiKey() {
        return apiKey;
    }
}