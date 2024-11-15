package com.g4t2project.g4t2project.service;
import com.g4t2project.g4t2project.DTO.EmailDetailsDto;
import com.g4t2project.g4t2project.entity.Role;
import com.g4t2project.g4t2project.entity.Admin;
import com.g4t2project.g4t2project.entity.Client;
import com.g4t2project.g4t2project.entity.User;
import com.g4t2project.g4t2project.entity.Worker;
import com.g4t2project.g4t2project.repository.UserRepository;
import com.g4t2project.g4t2project.repository.WorkerRepository;
import com.g4t2project.g4t2project.repository.AdminRepository;
import com.g4t2project.g4t2project.repository.ClientRepository;
import com.g4t2project.g4t2project.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;

import java.util.Set;
import java.util.UUID;

@Service
public class RegistrationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private WorkerRepository workerRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private RoleRepository roleRepository;

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

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName("ROLE_USER").orElseGet(() -> {
            Role newUserRole = new Role();
            newUserRole.setName("ROLE_USER");
            return roleRepository.save(newUserRole);
        });
        roles.add(userRole);

        // Check if the username is "root" and assign admin role
        if ("root".equals(user.getUsername())) {
            Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElseGet(() -> {
                Role newAdminRole = new Role();
                newAdminRole.setName("ROLE_ADMIN");
                return roleRepository.save(newAdminRole);
            });
            roles.add(adminRole);
        }

        user.setRoles(roles);

        String token = UUID.randomUUID().toString();
        user.setTwoFactorToken(token);

        User savedUser = userRepository.save(user);

        if ("root".equals(user.getUsername())) {
            Admin admin = new Admin();
            admin.setUser(savedUser);
            admin.setName(user.getName());
            registerAdmin(admin);
        }
        
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

    public User assignAdminRole(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElseGet(() -> {
            Role newAdminRole = new Role();
            newAdminRole.setName("ROLE_ADMIN");
            return roleRepository.save(newAdminRole);
        });
        user.getRoles().add(adminRole);

        return userRepository.save(user);
    }

    public Client registerClient(Client client) {
        return clientRepository.save(client);
    }

    public Worker registerWorker(Worker worker) {
        try {
            System.out.println("Registering worker: " + worker.getName());
            // Get admin with ID 1 (using Long instead of int)
            Admin admin = adminRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
            System.out.println("Found admin: " + admin.getName());
            worker.setAdmin(admin);
            Worker savedWorker = workerRepository.save(worker);
            System.out.println("Worker saved successfully");
            return savedWorker;
        } catch (Exception e) {
            System.err.println("Error registering worker: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public Admin registerAdmin(Admin admin) {
        // Assign ROLE_ADMIN to the user
        User user = admin.getUser();
        if (user != null) {
            Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElseGet(() -> {
                Role newAdminRole = new Role();
                newAdminRole.setName("ROLE_ADMIN");
                return roleRepository.save(newAdminRole);
            });
            user.getRoles().add(adminRole);
            userRepository.save(user);
        }
        return adminRepository.save(admin);
    }
}