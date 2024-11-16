package com.g4t2project.g4t2project.controllers;

import com.g4t2project.g4t2project.DTO.LeaveApplicationDTO;
import com.g4t2project.g4t2project.entity.*;
import com.g4t2project.g4t2project.service.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
    public ResponseEntity<?> applyForLeave(@RequestBody LeaveApplication leaveApplication) {
        try {
            LeaveApplication savedApplication = leaveApplicationService.applyForLeave(leaveApplication);
            
            // Determine if the leave application is submitted too late for reassignment
            LocalDateTime startDateTime = leaveApplication.getStartDate().atStartOfDay();

            Duration timeUntilLeave = Duration.between(LocalDateTime.now(), startDateTime);
            boolean isLateNotice = timeUntilLeave.compareTo(Duration.ofHours(3)) < 0;
            System.out.println("Time until leave: " + timeUntilLeave);
            System.out.println(isLateNotice);
            if (isLateNotice) {
                // Notify the client that rescheduling may not be possible
                CleaningTask task = leaveApplicationService.getTopTaskByWorker(leaveApplication.getWorker());
                System.out.println("Task ID: " + task.getTaskId());
                notificationService.notifyClientForLateLeave(task.getProperty().getClient(), task);
            } else {
                // Otherwise, notify the admin of a pending MC requirement
                notificationService.notifyAdminForPendingMC(leaveApplication);
            }
            
            // Return the leaveId in the response
            return ResponseEntity.ok(Map.of(
                "message", "Leave application submitted successfully",
                "leaveId", savedApplication.getLeaveApplicationId()
            ));
        } catch (Exception e) {
            e.printStackTrace(); // For debugging purposes
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to submit leave application: " + e.getMessage()));
        }
    }

    @PostMapping("/{leaveId}/upload-mc")
    public ResponseEntity<String> uploadMcDocument(
            @PathVariable int leaveId,
            @RequestParam("mcDocument") MultipartFile mcDocument) {
        try {
            leaveApplicationService.uploadMcDocument(leaveId, mcDocument);
            return ResponseEntity.ok("MC document uploaded successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error uploading MC document: " + e.getMessage());
        }
    }

    @PostMapping("/approve/{leaveId}")
    public ResponseEntity<String> approveLeave(@PathVariable int leaveId) {
        try {
            leaveApplicationService.approveLeave(leaveId);
            
            // Retrieve the leave application to notify the client if necessary
            LeaveApplication leaveApplication = leaveApplicationService.getLeaveApplicationById(leaveId);
            
            try {
                CleaningTask task = leaveApplicationService.getTaskForWorker(leaveApplication.getWorker());
                if (task != null && task.getProperty() != null && task.getProperty().getClient() != null) {
                    Client client = task.getProperty().getClient();
                    // Notify client of the rescheduling if applicable
                    notificationService.notifyClientForReschedule(client, task);
                }
            } catch (RuntimeException e) {
                // Log the error but don't fail the approval
                System.err.println("Warning: Could not find task for worker or notify client: " + e.getMessage());
            }

            return ResponseEntity.ok("Leave approved successfully");
        } catch (Exception e) {
            e.printStackTrace(); // Add this for debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                   .body("Failed to approve leave: " + e.getMessage());
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

    @GetMapping("/all")
    public ResponseEntity<List<LeaveApplicationDTO>> getAllLeaveApplications() {
        try {
            List<LeaveApplicationDTO> leaveApplications = leaveApplicationService.getAllLeaveApplications();
            return ResponseEntity.ok(leaveApplications);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}