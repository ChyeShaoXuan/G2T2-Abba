package com.g4t2project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import com.g4t2project.g4t2project.entity.*;
import com.g4t2project.g4t2project.service.*;
@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private AdminService adminService;

    @PostMapping
    public Admin createAdmin(@RequestBody Admin admin) {
        return adminService.saveUser(admin);
    }

    @GetMapping
    public List<Admin> getAllUsers() {
        return adminService.getAllAdmins();
    }
}