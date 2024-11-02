package com.g4t2project.g4t2project.controllers;

import com.g4t2project.g4t2project.entity.LeaveApplication;
import com.g4t2project.g4t2project.service.LeaveApplicationService;
import com.g4t2project.g4t2project.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/leave")
@CrossOrigin(origins = "http://localhost:3000")
public class LeaveController {

    @Autowired
    private LeaveApplicationService leaveApplicationService;

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/apply")
    public ResponseEntity<String> applyForLeave(@RequestBody LeaveApplication leaveApplication) {
        try {
            leaveApplicationService.applyForLeave(leaveApplication);
            notificationService.notifyAdminForPendingMC(leaveApplication);  // Notify admin of the new leave application
            return ResponseEntity.ok("Leave application submitted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to submit leave application: " + e.getMessage());
        }
    }

    @PostMapping("/upload-mc")
    public ResponseEntity<String> uploadMcDocument(@RequestParam("leaveId") int leaveId, @RequestParam("mcDocument") MultipartFile mcDocument) {
        try {
            leaveApplicationService.uploadMcDocument(leaveId, mcDocument);
            return ResponseEntity.ok("MC document uploaded successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload MC document: " + e.getMessage());
        }
    }

    @PostMapping("/approve")
    public ResponseEntity<String> approveLeave(@RequestBody int leaveId) {
        try {
            leaveApplicationService.approveLeave(leaveId);
            
            LeaveApplication leaveApplication = leaveApplicationService.getLeaveApplicationById(leaveId);
            // notificationService.notifyClientForReschedule(leaveApplication.getClient(), leaveApplication.getTask());  // Notify client if applicable
            
            return ResponseEntity.ok("Leave approved successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to approve leave: " + e.getMessage());
        }
    }
}


