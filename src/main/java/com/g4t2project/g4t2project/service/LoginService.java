package com.g4t2project.g4t2project.service;

import com.g4t2project.g4t2project.DTO.LoginRequest;
import com.g4t2project.g4t2project.entity.User;
import com.g4t2project.g4t2project.repository.UserRepository;
import com.g4t2project.g4t2project.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;


    public String login(LoginRequest loginRequest) {
        User user = userRepository.findByName(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return jwtUtil.generateToken(user.getUsername(), user.getRole());
    }
}