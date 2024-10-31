package com.g4t2project.g4t2project.controllers;


import com.g4t2project.g4t2project.entity.*;
import com.g4t2project.g4t2project.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com. g4t2project.g4t2project.DTO.ClientDTO;
import com.g4t2project.g4t2project.DTO.StatsDTO;

import java.util.List;
import java.util.Map;

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
    
    @PutMapping("/workers/{workerId}")
    public ResponseEntity<Worker> updateWorker(@PathVariable Long workerId, @RequestBody Worker worker) {
        Worker updatedWorker = adminService.updateWorker(workerId, worker);
        return ResponseEntity.ok(updatedWorker);
    }

    @PutMapping("/leave_applications/{id}")
    public ResponseEntity<Void> updateLeaveApplicationStatus(@PathVariable int id, @RequestParam LeaveApplication.Status status) {
        adminService.updateLeaveApplicationStatus(id, status);
        return ResponseEntity.noContent().build();
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

    // Adding client record in database under a specific admin
    @PostMapping("/{adminId}/clients")
    public ResponseEntity<Client> addClientUnderAdmin(
            @PathVariable Long adminId,
            @RequestBody ClientDTO clientDTO) {
        
        Client createdClient = adminService.addClientUnderAdmin(adminId, clientDTO);
        return ResponseEntity.ok(createdClient);
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

    @GetMapping("/clients")
    public ResponseEntity<List<ClientDTO>> getAllClients() {
        List<ClientDTO> clients = adminService.getAllClients();
        return ResponseEntity.ok(clients);
    }


    @PutMapping("/clients/{clientId}")
    public ResponseEntity<Client> updateClient(@PathVariable Long clientId, @RequestBody ClientDTO clientDTO) {
        Client updatedClient = adminService.updateClient(clientId, clientDTO);
        return ResponseEntity.ok(updatedClient);
    }
    @GetMapping("/stats")
    public ResponseEntity<Map<String, StatsDTO>> getAllStats() {
            Map<String, StatsDTO> stats = adminService.getAllStats();
            return ResponseEntity.ok(stats);
        }
    
    @GetMapping("/get_all_worker_ids")
    public ResponseEntity<List<Long>> getAllWorkerIds() {
        List<Long> workerIds = adminService.getAllWorkerIds();
        return ResponseEntity.ok(workerIds);
    }
    @GetMapping("/{workerId}/hours")
    public ResponseEntity<List<WorkerHours>> getWorkerHoursByWorkerId(@PathVariable Long workerId) {
        List<WorkerHours> workerHours = adminService.getWorkerHoursByWorkerId(workerId);
        return ResponseEntity.ok(workerHours);
    }

    }

    
