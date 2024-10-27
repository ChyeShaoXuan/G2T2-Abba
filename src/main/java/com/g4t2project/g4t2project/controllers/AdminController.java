package com.g4t2project.g4t2project.controllers;


import com.g4t2project.g4t2project.entity.*;
import com.g4t2project.g4t2project.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "http://localhost:3000")
public class AdminController {

    @Autowired
    private AdminService adminService;


    @PostMapping("/{adminId}/workers")
    public ResponseEntity<Worker> addWorker(@PathVariable Long adminId, @RequestBody Worker worker) {
        Worker newWorker = adminService.addWorkerUnderAdmin(adminId, worker);
        return ResponseEntity.ok(newWorker);
    }

    // Remove worker under an admin
    @DeleteMapping("/{adminId}/workers/{workerId}")
    public ResponseEntity<Admin> removeWorker(@PathVariable Long adminId, @PathVariable Long workerId) {
        Admin admin = adminService.removeWorkerUnderAdmin(adminId, workerId);
        return ResponseEntity.ok(admin);
    }
    
    @PutMapping("/workers/{id}")
    public ResponseEntity<Worker> updateWorker(@PathVariable Long id, @RequestBody Worker updatedWorker) {
        Worker updated = adminService.updateWorker(id, updatedWorker);
        return ResponseEntity.ok(updated);
    }


    @PutMapping("/leave_applications/{id}")
    public ResponseEntity<Void> updateLeaveApplicationStatus(@PathVariable int id, @RequestParam LeaveApplication.Status status) {
        adminService.updateLeaveApplicationStatus(id, status);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{adminId}/clients")
    public ResponseEntity<String> addClientUnderAdmin(@PathVariable Long adminId, @RequestBody Client client) {
        try {
            adminService.addClientUnderAdmin(adminId, client);
            return new ResponseEntity<>("Client added successfully under Admin", HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // Remove a client under a specific admin
    @DeleteMapping("/{adminId}/clients/{clientId}")
    public ResponseEntity<String> removeClientUnderAdmin(@PathVariable Long adminId, @PathVariable Long clientId) {
        try {
            adminService.removeClientUnderAdmin(adminId, clientId);
            return new ResponseEntity<>("Client removed successfully from Admin", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // Update client info under a specific admin
    @PutMapping("/{adminId}/clients/{clientId}")
    public ResponseEntity<String> updateClientUnderAdmin(@PathVariable Long adminId, @PathVariable Long clientId, @RequestBody Client updatedClient) {
        try {
            adminService.updateClientUnderAdmin(adminId, clientId, updatedClient);
            return new ResponseEntity<>("Client updated successfully under Admin", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    
    @PutMapping("/{adminId}/tasks/{taskId}/assign/{workerId}")
    public ResponseEntity<String> assignTaskToWorker(@PathVariable int taskId, @PathVariable Long workerId) {
        try {
            adminService.assignTaskToWorker(taskId, workerId);
            return new ResponseEntity<>("Task assigned successfully", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping("/workers")
    public ResponseEntity<List<Worker>> getAllWorkers() {
        List<Worker> workers = adminService.getAllWorkers();
        return ResponseEntity.ok(workers);
    }
}
