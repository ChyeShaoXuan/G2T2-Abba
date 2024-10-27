package com.g4t2project.g4t2project.controllers;

import com.g4t2project.g4t2project.entity.*;
import com.g4t2project.g4t2project.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/clients")
@CrossOrigin(origins = "http://localhost:3000")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PostMapping("/{clientId}/placeOrder")
    public ResponseEntity<CleaningTask> placeOrder(
        @PathVariable Long clientId,
        @RequestBody Map<String, Object> payload) {

        int packageID = (int) payload.get("packageID");
        int propertyID = (int) payload.get("propertyID");
        String propertyType = (String) payload.get("propertyType");
        int numberOfRooms = (int) payload.get("numberOfRooms");
        CleaningTask.Shift shift = CleaningTask.Shift.valueOf((String) payload.get("shift"));
        String dateStr = (String) payload.get("date");
        LocalDate date = LocalDate.parse(dateStr);
        Long preferredWorkerId = payload.get("preferredWorkerId") != null ? Long.valueOf(payload.get("preferredWorkerId").toString()) : null;

        CleaningTask task = clientService.placeOrder(clientId, packageID, propertyID, propertyType, numberOfRooms, shift, date, preferredWorkerId);
        return ResponseEntity.ok(task);
    }

    @PostMapping("/{clientId}/rateSession")
    public ResponseEntity<Feedback> rateSession(@PathVariable Long clientId, @RequestParam int taskID, @RequestParam int rating, @RequestParam String comments) 
    {
        Feedback feedback = clientService.rateSession(clientId, taskID, rating, comments);
        return ResponseEntity.ok(feedback);
    }

    @PostMapping("/{clientId}/addProperty")
    public ResponseEntity<Property> addProperty(
            @PathVariable Long clientId,
            @RequestBody Map<String, Object> payload) {

        String address = (String) payload.get("address");
        String postalCode = (String) payload.get("postalCode");
        int packageID = (int) payload.get("packageID");
        double latitude = Double.parseDouble(payload.get("latitude").toString());
        double longitude = Double.parseDouble(payload.get("longitude").toString());

        Property property = clientService.addProperty(clientId, address, postalCode, packageID, latitude, longitude);
        return ResponseEntity.ok(property);
    }

    @PutMapping("/{clientId}/modifyProperty/{propertyID}")
    public ResponseEntity<Boolean> modifyProperty(
            @PathVariable Long clientId,
            @PathVariable int propertyID,
            @RequestBody Map<String, String> payload) {

        String newAddress = payload.get("newAddress");
        String newPostalCode = payload.get("newPostalCode");

        boolean success = clientService.modifyProperty(clientId, propertyID, newAddress, newPostalCode);
        return ResponseEntity.ok(success);
    }

    @PostMapping("/{clientId}/submitFeedback")
    public ResponseEntity<Boolean> submitFeedback(
            @PathVariable Long clientId,
            @RequestParam int taskID,
            @RequestParam int rating,
            @RequestParam String comments) {

        boolean success = clientService.submitFeedback(clientId, taskID, rating, comments);
        return ResponseEntity.ok(success);
    }

    @PostMapping("/{clientId}/selectPackage")
    public ResponseEntity<CleaningPackage> selectPackage(
            @PathVariable Long clientId,
            @RequestParam int propertyID,
            @RequestParam int packageID) {

        CleaningPackage selectedPackage = clientService.selectPackage(clientId, propertyID, packageID);
        return ResponseEntity.ok(selectedPackage);
    }


}
