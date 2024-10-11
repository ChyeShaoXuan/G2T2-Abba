package com.g4t2project.g4t2project.controllers;


import com.g4t2project.g4t2project.entity.*;
import com.g4t2project.g4t2project.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;


    @PostMapping("/{adminId}/workers")
    public ResponseEntity<Admin> addWorker(@PathVariable Long adminId, @RequestBody Worker worker) {
        Admin admin = adminService.addWorkerUnderAdmin(adminId, worker);
        return ResponseEntity.ok(admin);
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


    @PutMapping("/leave-applications/{id}")
    public ResponseEntity<Void> updateLeaveApplicationStatus(@PathVariable int id, @RequestParam LeaveApplication.Status status) {
        adminService.updateLeaveApplicationStatus(id, status);
        return ResponseEntity.noContent().build();
    }

    
}
