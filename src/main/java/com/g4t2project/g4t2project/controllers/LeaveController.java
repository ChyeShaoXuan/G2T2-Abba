// File: src/main/java/com/g4t2project/g4t2project/controllers/LeaveController.java
package com.g4t2project.g4t2project.controllers;

import com.g4t2project.g4t2project.entity.LeaveApplication;
import com.g4t2project.g4t2project.entity.CleaningTask;
import com.g4t2project.g4t2project.entity.Client;
import com.g4t2project.g4t2project.service.LeaveApplicationService;
import com.g4t2project.g4t2project.service.NotificationService;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
            
            // Determine if the leave application is submitted too late for reassignment
            Duration timeUntilLeave = Duration.between(LocalDateTime.now(), leaveApplication.getStartDate());
            boolean isLateNotice = timeUntilLeave.compareTo(Duration.ofHours(3)) < 0;
            
            if (isLateNotice) {
                // Notify the client that rescheduling may not be possible
                CleaningTask task = leaveApplicationService.getTaskForWorker(leaveApplication.getWorker());
                notificationService.notifyClientForLateLeave(task.getProperty().getClient(), task);
            } else {
                // Otherwise, notify the admin of a pending MC requirement
                notificationService.notifyAdminForPendingMC(leaveApplication);
            }
            
            return ResponseEntity.ok("Leave application submitted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to submit leave application: " + e.getMessage());
        }
    }

    @PostMapping(value = "/upload-mc", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadMcDocument(
            @RequestParam("leaveId") int leaveId,
            @RequestParam("mcDocument") MultipartFile mcDocument) {
        try {
            System.out.println("Received upload request:");
            System.out.println("Leave ID: " + leaveId);
            System.out.println("File name: " + mcDocument.getOriginalFilename());
            System.out.println("File size: " + mcDocument.getSize());
            System.out.println("Content type: " + mcDocument.getContentType());
            
            leaveApplicationService.uploadMcDocument(leaveId, mcDocument);
            return ResponseEntity.ok("MC document uploaded successfully");
        } catch (Exception e) {
            System.err.println("Error in controller:");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to upload MC document: " + e.getMessage());
        }
    }

    @PostMapping("/approve")
    public ResponseEntity<String> approveLeave(@RequestBody int leaveId) {
        try {
            leaveApplicationService.approveLeave(leaveId);
            
            // Retrieve the leave application to notify the client if necessary
            LeaveApplication leaveApplication = leaveApplicationService.getLeaveApplicationById(leaveId);
            CleaningTask task = leaveApplicationService.getTaskForWorker(leaveApplication.getWorker());
            Client client = task.getProperty().getClient();

            // Notify client of the rescheduling if applicable
            notificationService.notifyClientForReschedule(client, task);

            return ResponseEntity.ok("Leave approved successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to approve leave: " + e.getMessage());
        }
    }

    @GetMapping("/{leaveId}/mc-document")
    public ResponseEntity<byte[]> getMcDocument(@PathVariable int leaveId) {
        try {
            LeaveApplication leaveApplication = leaveApplicationService.getLeaveApplicationById(leaveId);
            byte[] mcDocument = leaveApplication.getMcDocument();
            
            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, 
                    "attachment; filename=\"" + leaveApplication.getMcDocumentUrl() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(mcDocument);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(null);
        }
    }
}
