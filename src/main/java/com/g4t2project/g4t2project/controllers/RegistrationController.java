package com.g4t2project.g4t2project.controllers;

import com.g4t2project.g4t2project.entity.*;
import com.g4t2project.g4t2project.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/authentication")
@CrossOrigin(origins = "http://localhost:3000" , allowCredentials = "true") // Allow requests from this origin
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;


    // controller receive POST req at endpt w user data
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {

        if (user.getUsername() == null || user.getEmail() == null || user.getPassword() == null) {
            return ResponseEntity.badRequest().body("One or more of the fields are empty.");
        }


        // calling registerUser method of registrationService
        User registeredUser = registrationService.registerUser(user);

        if ("Client".equalsIgnoreCase(user.getRole())) {
            Client client = new Client();
            client.setUser(registeredUser);
            client.setName(user.getName());
            client.setEmail(user.getEmail());
            client.setPhoneNumber(user.getPhoneNumber());
            registrationService.registerClient(client);
        } else if ("Worker".equalsIgnoreCase(user.getRole())) {
            Worker worker = new Worker();
            worker.setUser(registeredUser);
            worker.setName(user.getName());
            worker.setPhoneNumber(user.getPhoneNumber());
            worker.setemailID(user.getEmail());
            registrationService.registerWorker(worker);
        } 

        // save user in the repo 
        return ResponseEntity.ok(registeredUser);

    }


}