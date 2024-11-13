package com.g4t2project.g4t2project.controllers;

import com.g4t2project.g4t2project.DTO.LoginRequest;
import com.g4t2project.g4t2project.DTO.LoginResponse;
import com.g4t2project.g4t2project.entity.User;
import com.g4t2project.g4t2project.entity.Role;
import com.g4t2project.g4t2project.repository.UserRepository;
import com.g4t2project.g4t2project.service.CustomUserDetailsService;
import com.g4t2project.g4t2project.util.JwtUtil;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authentication")
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

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
        return ResponseEntity.ok().body(new LoginResponse(
                    "Login successful",
                    token,
                    isExistingUser.getName(),
                    isExistingUser.getRole(),
                    isExistingUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
            ));

    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(500).body("Internal server error");
    }
}

@GetMapping("/user")
public ResponseEntity<User> getUser(@AuthenticationPrincipal UserDetails userDetails) {
    String username = userDetails.getUsername();
    User user = customUserDetailsService.loadUserEntityByUsername(username);
    return ResponseEntity.ok(user);
}



}