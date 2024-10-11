package com.g4t2project.g4t2project.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.g4t2project.g4t2project.repository.*;
import com.g4t2project.g4t2project.entity.*;

import java.util.Optional;

@Service
public class AdminService {
    
    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private LeaveApplicationRepository leaveApplicationRepository;

    @Autowired
    private WorkerRepository workerRepository;

    @Autowired
    private ClientRepository clientRepository;


    public Admin addWorkerUnderAdmin(Long adminId, Worker worker) {
        Admin admin = adminRepository.findById(adminId).orElseThrow(() -> new RuntimeException("Admin not found"));
        admin.addWorker(worker);
        return adminRepository.save(admin);
    }

    
    public Admin removeWorkerUnderAdmin(Long adminId, Long workerId) {
        Admin admin = adminRepository.findById(adminId).orElseThrow(() -> new RuntimeException("Admin not found"));
        Worker worker = workerRepository.findById(workerId).orElseThrow(() -> new RuntimeException("Worker not found"));
        admin.removeWorker(worker);
        return adminRepository.save(admin);
    }
   
    public Worker updateWorker(Long workerId, Worker updatedWorker) {
        Optional<Worker> existingWorkerOpt = workerRepository.findById(workerId);
        if (existingWorkerOpt.isPresent()) {
            Worker existingWorker = existingWorkerOpt.get();
            existingWorker.setPhoneNumber(updatedWorker.getPhoneNumber());
            existingWorker.setShortBio(updatedWorker.getShortBio());
            existingWorker.setDeployed(updatedWorker.getDeployed());
            return workerRepository.save(existingWorker);
        } else {
            throw new RuntimeException("Worker not found");
        }
    }

    public void updateLeaveApplicationStatus(int leaveApplicationId, LeaveApplication.Status status) {
        Optional<LeaveApplication> leaveApplicationOpt = leaveApplicationRepository.findById(leaveApplicationId);
        if (leaveApplicationOpt.isPresent()) {
            LeaveApplication leaveApplication = leaveApplicationOpt.get();
            leaveApplication.setStatus(status);
            leaveApplicationRepository.save(leaveApplication);
        } else {
            throw new RuntimeException("Leave Application not found");
        }
    }

    public Admin addClientUnderAdmin(Long adminId, Client client) {
        Admin admin = adminRepository.findById(adminId).orElseThrow(() -> new RuntimeException("Admin not found"));
        admin.addClient(client);  // Using the method in Admin entity
        return adminRepository.save(admin);
    }

    // Remove a Client under Admin
    public Admin removeClientUnderAdmin(Long adminId, Long clientId) {
        Admin admin = adminRepository.findById(adminId).orElseThrow(() -> new RuntimeException("Admin not found"));
        Client client = clientRepository.findById(clientId).orElseThrow(() -> new RuntimeException("Client not found"));
        admin.removeClient(client);  // Using the method in Admin entity
        return adminRepository.save(admin);
    }

    // Update Client Info
    public Admin updateClientUnderAdmin(Long adminId, Long clientId, Client updatedClient) {
        Admin admin = adminRepository.findById(adminId).orElseThrow(() -> new RuntimeException("Admin not found"));
        admin.updateClientInfo(clientId, updatedClient);  // Using the method in Admin entity
        return adminRepository.save(admin);
    }



    
}
