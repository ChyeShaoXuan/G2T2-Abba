package com.g4t2project.g4t2project.controllers;

import com.g4t2project.g4t2project.entity.User;
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

        // generate 2FA token

        // save user in the repo 
        return ResponseEntity.ok(registeredUser);

        // send confirmation email to user

        // controller sends back http response to client w user data 

    }


}