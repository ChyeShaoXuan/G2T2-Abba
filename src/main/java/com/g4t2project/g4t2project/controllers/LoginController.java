package com.g4t2project.g4t2project.controllers;

import com.g4t2project.g4t2project.DTO.LoginRequest;
import com.g4t2project.g4t2project.DTO.LoginResponse;
import com.g4t2project.g4t2project.entity.User;
import com.g4t2project.g4t2project.repository.UserRepository;
import com.g4t2project.g4t2project.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authentication")
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    public LoginController(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
    try {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        // Check if username or password is empty
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            return ResponseEntity.badRequest().body("Username or password is empty!");
        }

        // Check if user exists
        User isExistingUser = userRepository.findByName(username)
                .orElse(null);

        if (isExistingUser == null) {
            return ResponseEntity.badRequest().body("User doesn't exist.");
        }

        // Check if password matches
        boolean comparePW = passwordEncoder.matches(password, isExistingUser.getPassword());

        if (!comparePW) {
            return ResponseEntity.badRequest().body("Password doesn't match.");
        }

        // Generate JWT
        String token = jwtUtil.generateToken(isExistingUser.getUsername(), isExistingUser.getRole());

        // Return success if all checks pass
        return ResponseEntity.ok().body(new LoginResponse("Login successful", token, isExistingUser.getUsername(), isExistingUser.getRole()));

    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(500).body("Internal server error");
    }
}



}