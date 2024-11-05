package com.g4t2project.g4t2project.service;
import com.g4t2project.g4t2project.DTO.EmailDetailsDto;
import com.g4t2project.g4t2project.entity.User;
import com.g4t2project.g4t2project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Optional;
import java.io.IOException;
import java.util.UUID;

@Service
public class RegistrationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SendMailService sendMailService;


    // process user data, save new users to db , w encoded pw
    public User registerUser(User user) {

        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            throw new RuntimeException("Email is already in use");
        }
        
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        String token = UUID.randomUUID().toString();
        user.setTwoFactorToken(token);

        User savedUser = userRepository.save(user);

        
        String verificationLink = "http://localhost:8080/authentication/verify?token=" + token;

        
        sendMailService.sendEnhancedMail(
            user.getEmail(),
            "Registration Confirmation",
            "Your Community",
            "Recipient Name",
            verificationLink
        );

        return savedUser;
        // userRepository does actual saving of user, 
        // returns saved user to controller
    }

}