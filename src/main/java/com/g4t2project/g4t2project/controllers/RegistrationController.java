package com.g4t2project.g4t2project.controllers;

import com.g4t2project.g4t2project.entity.User;
import com.g4t2project.g4t2project.entity.Admin;
import com.g4t2project.g4t2project.entity.Client;
import com.g4t2project.g4t2project.entity.Worker;
import com.g4t2project.g4t2project.repository.UserRepository;
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

    
    @Autowired
    private UserRepository userRepository;

    // controller receive POST req at endpt w user data
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {

        if (user.getUsername() == null || user.getEmail() == null || user.getPassword() == null) {
            return ResponseEntity.badRequest().body("One or more of the fields are empty.");
        }

        // if (userRepository.findByEmail(user.getEmail()) != null) {
        //     return ResponseEntity.badRequest().body("Email is already in use.");
        // }

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
            registrationService.registerWorker(worker);
        } else if ("Admin".equalsIgnoreCase(user.getRole())) {
            Admin admin = new Admin();
            admin.setUser(registeredUser);
            admin.setName(user.getName());
            registrationService.registerAdmin(admin);
        }

        // save user in the repo 
        return ResponseEntity.ok(registeredUser);

        // send confirmation email to user

        // controller sends back http response to client w user data 

    }


}