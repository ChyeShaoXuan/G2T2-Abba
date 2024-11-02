package com.g4t2project.g4t2project.controllers;

import com.g4t2project.g4t2project.entity.User;
import com.g4t2project.g4t2project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authentication")
public class VerificationController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/verify")
    public ResponseEntity<String> verifyUser(@RequestParam String token) {
        System.out.println("Received request to verify token: " + token);
        try {
            User user = userRepository.findByTwoFactorToken(token)
                    .orElseThrow(() -> new RuntimeException("Invalid token"));

            System.out.println("User found: " + user.getUsername());

            // Clear the 2FA token and mark the user as verified
            user.setTwoFactorToken(null);
            user.setVerified(true); 
            userRepository.save(user);

            System.out.println("User verified and saved: " + user.getUsername());
            String successUrl = "http://localhost:3000/auth/verify-success?status=success";
            String successHtml = "<html><body><p>Verification successful! Click <a href=\"" + successUrl + "\">here</a> to continue.</p></body></html>";
            return ResponseEntity.ok(successHtml);
        } catch (RuntimeException e) {
            System.out.println("Verification failed: " + e.getMessage());
            String failedUrl = "http://localhost:3000/auth/verify-success?status=failed";
            String failedHtml = "<html><body><p>Verification failed: " + e.getMessage() + ". Click <a href=\"" + failedUrl + "\">here</a> to try again.</p></body></html>";
            return ResponseEntity.badRequest().body(failedHtml);
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
            String errorUrl = "http://localhost:3000/auth/verify-success?status=error";
            String errorHtml = "<html><body><p>An error occurred: " + e.getMessage() + ". Click <a href=\"" + errorUrl + "\">here</a> to try again.</p></body></html>";
            return ResponseEntity.status(500).body(errorHtml);
        }
    }
}