package com.g4t2project.g4t2project.controllers;

import com.g4t2project.g4t2project.DTO.EmailDetailsDto;
import com.g4t2project.g4t2project.service.SendMailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.g4t2project.g4t2project.entity.User;
import com.g4t2project.g4t2project.repository.UserRepository;

import java.util.Optional;

@RestController
public class EmailController {

    @Autowired
    private SendMailService emailService;

    @Autowired
    private UserRepository userRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailController.class);

    @PostMapping("/sendMail")
    public void sendEnhancedMail(@RequestBody EmailDetailsDto details) {
        LOGGER.info("Sending an enhanced mail through Courier");

        Optional<User> optionalUser = userRepository.findByEmail(details.getRecipient());
        if (!optionalUser.isPresent()) {
            LOGGER.error("User not found for email: " + details.getRecipient());
            throw new RuntimeException("User not found for email: " + details.getRecipient());
        }

        User user = optionalUser.get();
        String verificationLink = "http://localhost:8080/authentication/verify?token=" + user.getTwoFactorToken();
        emailService.sendEnhancedMail(details.getRecipient(), details.getEmailSubject(), "Your Community", "Recipient Name", verificationLink);
    }
}