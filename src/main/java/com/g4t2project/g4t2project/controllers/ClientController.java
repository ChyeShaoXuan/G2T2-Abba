package com.g4t2project.g4t2project.controllers;

import com.g4t2project.g4t2project.entity.*;
import com.g4t2project.g4t2project.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PostMapping("/{clientId}/placeOrder")
    public ResponseEntity<CleaningTask> placeOrder(@PathVariable Long clientId, @RequestParam Integer packageID,
    @RequestParam Integer propertyID, @RequestParam CleaningTask.Shift shift, @RequestParam String date) {
        LocalDate localDate = LocalDate.parse(date);
        CleaningTask task = clientService.placeOrder(clientId, packageID, propertyID, shift, localDate);
        return ResponseEntity.ok(task);
    }

    @PostMapping("/{clientId}/rateSession")
    public ResponseEntity<Feedback> rateSession(@PathVariable Long clientId, @RequestParam int taskID, @RequestParam int rating, @RequestParam String comments) 
    {
        Feedback feedback = clientService.rateSession(clientId, taskID, rating, comments);
        return ResponseEntity.ok(feedback);
    }

}
