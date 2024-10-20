package com.g4t2project.g4t2project.controller;

import com.g4t2project.g4t2project.entity.LeaveApplication;
import com.g4t2project.g4t2project.service.LeaveApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/leave")
public class LeaveController {
    @Autowired
    private LeaveApplicationService leaveApplicationService;

    @PostMapping("/apply")
    public ResponseEntity<String> applyForLeave(@RequestBody LeaveApplication leaveApplication) {
        leaveApplicationService.applyForLeave(leaveApplication);
        return ResponseEntity.ok("Leave application submitted successfully");
    }

    @PostMapping("/upload-mc")
    public ResponseEntity<String> uploadMcDocument(@RequestParam("leaveId") int leaveId, @RequestParam("mcDocument") MultipartFile mcDocument) {
        leaveApplicationService.uploadMcDocument(leaveId, mcDocument);
        return ResponseEntity.ok("MC document uploaded successfully");
    }

    @PostMapping("/approve")
    public ResponseEntity<String> approveLeave(@RequestBody int leaveId) {
        leaveApplicationService.approveLeave(leaveId);
        return ResponseEntity.ok("Leave approved");
    }
}

