package com.g4t2project.g4t2project.controllers;

import com.g4t2project.g4t2project.entity.*;
import com.g4t2project.g4t2project.service.*;
import com.g4t2project.g4t2project.DTO.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/clients")
@CrossOrigin(origins = "http://localhost:3000")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PostMapping("/{clientId}/placeOrder")
    public ResponseEntity<cleaningTaskDTO> placeOrder(@PathVariable Long clientId, @RequestBody PlaceOrderRequestDTO request) {
        LocalDate localDate = LocalDate.parse(request.getDate());
        cleaningTaskDTO task = clientService.placeOrder(clientId, request.getPackageType(), request.getPropertyType(), request.getNumberOfRooms(), request.getShift(), localDate);
        return ResponseEntity.ok(task);
    }

    @GetMapping("/clientId/{clientName}")
    public ResponseEntity<Long> getClientIdByName(@PathVariable String clientName) {
        try {
            Long clientId = clientService.getClientIdByName(clientName);
            return ResponseEntity.ok(clientId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @PostMapping("/{clientId}/rateSession")
    public ResponseEntity<Feedback> rateSession(@PathVariable Long clientId, @RequestBody int taskID, @RequestBody int rating, @RequestBody String comments) 
    {
        Feedback feedback = clientService.rateSession(clientId, taskID, rating, comments);
        return ResponseEntity.ok(feedback);
    }

    @PostMapping("/{clientId}/addProperty")
    public ResponseEntity<Property> addProperty(
            @PathVariable Long clientId,
            @RequestBody Map<String, Object> payload) {

        String address = (String) payload.get("address");
        // String postalCode = (String) payload.get("postalCode");
        Long packageID = (Long) payload.get("packageID");
        double latitude = Double.parseDouble(payload.get("latitude").toString());
        double longitude = Double.parseDouble(payload.get("longitude").toString());

        Property property = clientService.addProperty(clientId, address, packageID, latitude, longitude);
        return ResponseEntity.ok(property);
    }

    @PutMapping("/{clientId}/modifyProperty/{propertyID}")
    public ResponseEntity<Boolean> modifyProperty(
            @PathVariable Long clientId,
            @PathVariable Long propertyID,
            @RequestBody Map<String, String> payload) {

        String newAddress = payload.get("newAddress");
        // String newPostalCode = payload.get("newPostalCode");

        boolean success = clientService.modifyProperty(clientId, propertyID, newAddress);
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
            @RequestParam Long propertyID,
            @RequestParam Long packageID) {

        CleaningPackage selectedPackage = clientService.selectPackage(clientId, propertyID, packageID);
        return ResponseEntity.ok(selectedPackage);
    }




}
