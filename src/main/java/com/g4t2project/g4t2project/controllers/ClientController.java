package com.g4t2project.g4t2project.controllers;

import com.g4t2project.g4t2project.entity.*;
import com.g4t2project.g4t2project.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;  

@RestController
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PostMapping("/{clientId}/placeOrder")
    public CleaningTask placeOrder(@PathVariable int clientId,
                                   @RequestParam int packageID,
                                   @RequestParam int propertyID,
                                   @RequestParam CleaningTask.Shift shift,
                                   @RequestParam String date) {
        LocalDate localDate = LocalDate.parse(date);
        return clientService.placeOrder(clientId, packageID, propertyID, shift, localDate);
    }

    @PostMapping("/{clientId}/rateSession")
    public Feedback rateSession(@PathVariable int clientId,
                                @RequestParam int taskID,
                                @RequestParam int rating,
                                @RequestParam String comments) {
        return clientService.rateSession(clientId, taskID, rating, comments);
    }

}
