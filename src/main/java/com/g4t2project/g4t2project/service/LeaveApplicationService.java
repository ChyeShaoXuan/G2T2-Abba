package com.g4t2project.g4t2project.service;

import com.g4t2project.g4t2project.entity.LeaveApplication;
import com.g4t2project.g4t2project.repository.LeaveApplicationRepository;
import com.g4t2project.g4t2project.repository.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class LeaveApplicationService {
    
    @Autowired
    private LeaveApplicationRepository leaveApplicationRepository;

    @Autowired
    private WorkerRepository workerRepository;

    private final String MC_UPLOAD_DIR = "uploads/mc/";

    public void applyForLeave(LeaveApplication leaveApplication) {
        leaveApplicationRepository.save(leaveApplication);
    }

    public void uploadMcDocument(int leaveId, MultipartFile mcDocument) {
        LeaveApplication leaveApplication = leaveApplicationRepository.findById(leaveId)
            .orElseThrow(() -> new RuntimeException("Leave Application not found for ID: " + leaveId));
        
        String fileName = leaveId + "_" + mcDocument.getOriginalFilename();
        Path filePath = Paths.get(MC_UPLOAD_DIR + fileName);
    
        try {
            Files.write(filePath, mcDocument.getBytes());
            leaveApplication.setMcDocumentUrl(filePath.toString());
            leaveApplicationRepository.save(leaveApplication);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload MC document", e);
        }
    }

    public void approveLeave(int leaveId) {
        LeaveApplication leaveApplication = leaveApplicationRepository.findById(leaveId)
            .orElseThrow(() -> new RuntimeException("Leave Application not found for ID: " + leaveId));
        
        leaveApplication.setStatus(LeaveApplication.Status.Approved);
        leaveApplicationRepository.save(leaveApplication);
    }

    public LeaveApplication getLeaveApplicationById(int leaveId) {
        return leaveApplicationRepository.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("Leave Application not found for ID: " + leaveId));
    }
}


