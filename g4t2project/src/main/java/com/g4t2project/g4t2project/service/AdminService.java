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
    private leaveApplicationRepository leaveApplicationRepository;

    @Autowired
    private WorkerRepository workerRepository;


    public Admin addWorkerUnderAdmin(Long adminId, Worker worker) {
        Admin admin = adminRepository.findById(adminId).orElseThrow(() -> new RuntimeException("Admin not found"));
        worker.setDirectSupervisor(admin.getName());
        workerRepository.save(worker);
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
            existingWorker.setWorkingHours(updatedWorker.getWorkingHours());
            existingWorker.setPhoneNo(updatedWorker.getPhoneNo());
            existingWorker.setDirectSupervisor(updatedWorker.getDirectSupervisor());
            existingWorker.setShortBio(updatedWorker.getShortBio());
            existingWorker.setDeployed(updatedWorker.getDeployed());
            return workerRepository.save(existingWorker);
        } else {
            throw new RuntimeException("Worker not found");
        }
    }

    public void updateLeaveApplicationStatus(Long leaveApplicationId, String status) {
        Optional<leaveApplication> leaveApplicationOpt = leaveApplicationRepository.findById(leaveApplicationId);
        if (leaveApplicationOpt.isPresent()) {
            leaveApplication leaveApplication = leaveApplicationOpt.get();
            leaveApplication.setStatus(status);
            leaveApplicationRepository.save(leaveApplication);
        } else {
            throw new RuntimeException("Leave Application not found");
        }
    }
}
