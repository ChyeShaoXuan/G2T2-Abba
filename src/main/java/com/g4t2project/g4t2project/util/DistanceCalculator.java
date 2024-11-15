package com.g4t2project.g4t2project.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.g4t2project.g4t2project.config.GoogleMapsConfig;
import com.google.maps.GeoApiContext;
import com.google.maps.DirectionsApi;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;

@Component
public class DistanceCalculator {

    private final GoogleMapsConfig googleMapsConfig;

    @Autowired
    public DistanceCalculator(GoogleMapsConfig googleMapsConfig) {
        this.googleMapsConfig = googleMapsConfig;
    }

    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) throws Exception {
        System.out.println("Calculating distance function called!!!!");
        // Initialize the GeoApiContext with the API key
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(googleMapsConfig.getApiKey())
                .build();

        // Create the Directions API request
        DirectionsResult result = DirectionsApi.newRequest(context)
                .origin(new LatLng(lat1, lon1))
                .destination(new LatLng(lat2, lon2))
                .mode(TravelMode.TRANSIT)  
                .await(); 

        // Check if a valid route is found and return the distance in kilometers
        if (result.routes.length > 0) {
            System.out.println("-----------------------------------------------");
            System.out.println("Distance between worker location and task location: " + result.routes[0].legs[0].distance.inMeters / 1000.0);
            System.out.println("Time taken to travel by public transport: " + result.routes[0].legs[0].duration.inSeconds / 60.0);
            System.out.println("-----------------------------------------------");
            return result.routes[0].legs[0].distance.inMeters / 1000.0;  // Convert meters to kilometers
        } else {
            throw new Exception("No route found between the points.");
        }
    }
}
