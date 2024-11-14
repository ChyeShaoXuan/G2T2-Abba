package com.g4t2project.g4t2project.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.g4t2project.g4t2project.DTO.workerDTO;
import com.g4t2project.g4t2project.entity.LeaveApplication;
import com.g4t2project.g4t2project.entity.Worker;
import com.g4t2project.g4t2project.service.WorkerService;

@RestController
@RequestMapping("/worker")
@CrossOrigin(origins = "http://localhost:3000/")
public class WorkerController {

    @Autowired
    private WorkerService workerService;

    // Endpoint for worker authentication
    @PostMapping("/authenticate")
    public ResponseEntity<Map<String, Object>> authenticateWorker(@RequestBody workerDTO loginRequest) {
        System.out.println("Received login request: " + loginRequest.getWorkerId() + ", " + loginRequest.getPassword());

        boolean isAuthenticated = workerService.authenticate(loginRequest.getWorkerId(), loginRequest.getPassword());
        Map<String, Object> response = new HashMap<>();
        response.put("success", isAuthenticated);
        response.put("message", isAuthenticated ? "Authentication successful." : "Invalid worker ID or password.");

        return new ResponseEntity<>(response, isAuthenticated ? HttpStatus.OK : HttpStatus.UNAUTHORIZED);
    }

    // Method to accept a task
    @PutMapping("/{workerId}/tasks/{taskId}/accept")
    public ResponseEntity<Boolean> acceptAssignedTask(@PathVariable Long workerId, @PathVariable int taskId) {
        boolean isAccepted = workerService.acceptAssignedTask(taskId, workerId);
        if (isAccepted) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
        }
    }

    // Method for workers to apply for leave
    @PostMapping("/{workerId}/leave")
    public ResponseEntity<String> applyLeave(@PathVariable Long workerId, @RequestBody LeaveApplication leaveApplication) {
        try {
            workerService.applyLeave(workerId, leaveApplication);
            return ResponseEntity.status(HttpStatus.CREATED).body("Leave application submitted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to submit leave application.");
        }
    }

    // Method for confirming task completion
    @PutMapping("/{workerId}/tasks/{taskId}/confirm-completion")
    public ResponseEntity<Boolean> confirmCompletion(@PathVariable Long workerId, @PathVariable int taskId) {
        boolean isConfirmed = workerService.confirmCompletion(taskId, workerId);
        if (isConfirmed) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
        }
    }

    @GetMapping("/workerId/{username}")
    public ResponseEntity<Integer> getWorkerIdByUsername(@PathVariable String username) {
        Integer workerId = workerService.getWorkerIdByUsername(username);
        return ResponseEntity.ok(workerId);
    }
    @GetMapping("/{workerId}/admin")
    public ResponseEntity<Long> getAdminIdByWorkerId(@PathVariable Long workerId) {
        Long adminId = workerService.getAdminIdByWorkerId(workerId);
        return ResponseEntity.ok(adminId);  // Return the adminId as response
    }

}
