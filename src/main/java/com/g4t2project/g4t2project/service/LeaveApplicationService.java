package com.g4t2project.g4t2project.service;

import com.g4t2project.g4t2project.DTO.LeaveApplicationDTO;
import com.g4t2project.g4t2project.entity.LeaveApplication;
import com.g4t2project.g4t2project.entity.Worker;
import com.g4t2project.g4t2project.entity.Client;
import com.g4t2project.g4t2project.entity.CleaningTask;
import com.g4t2project.g4t2project.repository.CleaningTaskRepository;
import com.g4t2project.g4t2project.repository.LeaveApplicationRepository;
import com.g4t2project.g4t2project.repository.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeaveApplicationService {

    @Autowired
    private LeaveApplicationRepository leaveApplicationRepository;

    @Autowired
    private CleaningTaskRepository cleaningTaskRepository;

    @Autowired
    private WorkerRepository workerRepository;

    @Autowired
    private NotificationService notificationService; 

    private final String MC_UPLOAD_DIR = "uploads/mc/";
    private final Duration MINIMUM_NOTICE_DURATION = Duration.ofHours(3);  // 3-hour minimum

    public void applyForLeave(LeaveApplication leaveApplication) {
        LocalDateTime now = LocalDateTime.now();

        // Set initial status to Pending
        leaveApplication.setStatus(LeaveApplication.Status.Pending);

        // Fetch the worker associated with the leave application
        Worker worker = leaveApplication.getWorker();
        System.out.println("Worker ID: " + worker.getWorkerId());

        // Set the admin from the worker's admin
        leaveApplication.setAdmin(worker.getAdmin());

        // Fetch the cleaning task associated with the worker
        CleaningTask task = getTopTaskByWorker(worker);
        System.out.println("Task ID: " + task.getTaskId());

        // Get the client from the cleaning task
        Client client = task.getProperty().getClient();  // Assuming CleaningTask has getClient() method

        LocalDateTime leaveStartDateTime = leaveApplication.getStartDate().atStartOfDay();

        // Check if the leave is too close to the task start date
        if (Duration.between(now, leaveStartDateTime).compareTo(MINIMUM_NOTICE_DURATION) < 0) {
            // Notify the client that it's too late to reschedule
            notificationService.notifyClientForLateLeave(client, task);
        } else {
            // Attempt to reallocate workers
            if (!reallocateWorker(leaveApplication)) {
                // Notify the client if no replacement is found
                notificationService.notifyClientForReschedule(client, task);
            }
        }

        // Save the leave application and notify the admin of the pending MC
        leaveApplicationRepository.save(leaveApplication);
        notificationService.notifyAdminForPendingMC(leaveApplication);
    }

    public boolean reallocateWorker(LeaveApplication leaveApplication) {
        Worker workerOnLeave = leaveApplication.getWorker();
        LocalDate leaveStartDate = leaveApplication.getStartDate();  // Use start date for single-day tasks
        LocalDate leaveEndDate = leaveApplication.getEndDate();  // For single-day tasks, start and end date are the same
    
        // Retrieve the task associated with the worker using CleaningTaskRepository
        CleaningTask taskOnLeave = getTaskForWorker(workerOnLeave);
    
        // Retrieve the client associated with the task (assuming CleaningTask has a Client association)
        Client client = taskOnLeave.getProperty().getClient();  // Get the associated client for the task
    
        // Only check for the leave start date (since no multi-day tasks are allowed)
        Optional<CleaningTask.Shift> shiftOpt = Optional.ofNullable(taskOnLeave.getShift());
    
        if (shiftOpt.isPresent()) {
            // Find an available worker for the day and shift
            Optional<Worker> availableWorkerOpt = workerRepository.findAvailableWorker(leaveStartDate, shiftOpt.get());
    
            if (availableWorkerOpt.isPresent()) {
                // Found an available worker, reallocate the task
                Worker availableWorker = availableWorkerOpt.get();
                taskOnLeave.setWorker(availableWorker);  // Reassign task to the available worker
                cleaningTaskRepository.save(taskOnLeave);  // Save the reassigned task
    
                // Notify the client of the reassignment
                notificationService.notifyClientForReschedule(client, taskOnLeave);
    
                return true; // Reallocation successful
            }
        }
    
        // If no suitable worker is found
        return false; // No worker was available for the reallocation
    }
    
    public CleaningTask getTopTaskByWorker(Worker worker) {
        Optional<CleaningTask> taskOpt = cleaningTaskRepository.findFirstByWorkerOrderByTaskIdDesc(worker);
        System.out.println("Task: " + taskOpt);
        return taskOpt.orElseThrow(() -> new RuntimeException("Cleaning task not found for worker ID: " + worker.getWorkerId()));
    }

    public CleaningTask getTaskForWorker(Worker worker) {
        Optional<CleaningTask> taskOpt = cleaningTaskRepository.findTaskByWorker(worker);
        return taskOpt.orElseThrow(() -> new RuntimeException("Cleaning task not found for worker ID: " + worker.getWorkerId()));
    }

    public void uploadMcDocument(int leaveId, MultipartFile mcDocument) {
        try {
            LeaveApplication leaveApplication = leaveApplicationRepository.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("Leave Application not found for ID: " + leaveId));
            
            leaveApplication.setMcDocument(mcDocument.getBytes());
            leaveApplication.setMcDocumentUrl(mcDocument.getOriginalFilename());
            leaveApplication.setMcDocumentSubmitted(true);
            
            leaveApplicationRepository.save(leaveApplication);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload MC document: " + e.getMessage(), e);
        }
    }

    public byte[] getMcDocument(int leaveId) {
        LeaveApplication leaveApplication = leaveApplicationRepository.findById(leaveId)
            .orElseThrow(() -> new RuntimeException("Leave Application not found"));
        return leaveApplication.getMcDocument();
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

    public List<LeaveApplicationDTO> getAllLeaveApplications() {
        try {
            System.out.println("Fetching all leave applications...");
            List<LeaveApplication> leaveApplications = leaveApplicationRepository.findAll();
            System.out.println("Found " + leaveApplications.size() + " leave applications");
            
            return leaveApplications.stream()
                    .map(leaveApplication -> {
                        try {
                            System.out.println("Processing leave application ID: " + leaveApplication.getLeaveApplicationId());
                            
                            // Get worker name safely
                            String workerName = "Unknown";
                            if (leaveApplication.getWorker() != null) {
                                workerName = leaveApplication.getWorker().getName();
                            }
                            
                            // Get other fields safely
                            String leaveType = leaveApplication.getLeaveType() != null ? 
                                leaveApplication.getLeaveType().toString() : "Unknown";
                            String status = leaveApplication.getStatus() != null ? 
                                leaveApplication.getStatus().toString() : "Unknown";
                            
                            return new LeaveApplicationDTO(
                                    leaveApplication.getLeaveApplicationId(),
                                    workerName,
                                    leaveType,
                                    leaveApplication.getStartDate(),
                                    leaveApplication.getEndDate(),
                                    status
                            );
                        } catch (Exception e) {
                            System.err.println("Error processing leave application: " + e.getMessage());
                            e.printStackTrace();
                            throw e;
                        }
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error in getAllLeaveApplications: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}