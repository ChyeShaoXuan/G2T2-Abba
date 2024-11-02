package com.g4t2project.g4t2project.controllers;

import com.g4t2project.g4t2project.entity.User;
import com.g4t2project.g4t2project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/authentication")
public class VerificationController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/verify")
    public void verifyUser(@RequestParam String token, HttpServletResponse response) throws IOException {
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
            response.sendRedirect("http://localhost:3000/auth/verify-success?status=success");
        } catch (RuntimeException e) {
            System.out.println("Verification failed: " + e.getMessage());
            response.sendRedirect("http://localhost:3000/auth/verify-success?status=failed");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("http://localhost:3000/auth/verify-success?status=error");
        }
    }
}