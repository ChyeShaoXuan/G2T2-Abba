package com.g4t2project.g4t2project.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.g4t2project.g4t2project.repository.AdminRepository;
import com.g4t2project.g4t2project.entity.*;

import java.util.List;
@Service
public class AdminService {
    @Autowired
    private AdminRepository adminRepository;

    public Admin saveUser(Admin admin) {
        return adminRepository.save(admin);
    }

    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }
}
