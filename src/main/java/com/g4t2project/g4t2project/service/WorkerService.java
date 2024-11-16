package com.g4t2project.g4t2project.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.g4t2project.g4t2project.DTO.*;
import com.g4t2project.g4t2project.entity.*;
import com.g4t2project.g4t2project.repository.*;

@Service
public class WorkerService {

    @Autowired
    private WorkerRepository workerRepository;

    @Autowired
    private CleaningTaskRepository cleaningTaskRepository;

    @Autowired
    private LeaveApplicationRepository leaveApplicationRepository;

    // Authentication method for worker login
    public boolean authenticate(Long username, String password) {
        Worker worker = workerRepository.findById(username).orElse(null);
        return worker != null && worker.getEmailId().equals(password);
    }

    public boolean acceptAssignedTask(int taskId, Long workerId) {
        CleaningTask task = cleaningTaskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        Worker worker = workerRepository.findById(workerId)
                .orElseThrow(() -> new RuntimeException("Worker not found"));

        if (task.getWorker().getWorkerId().equals(workerId) && task.getStatus() == CleaningTask.Status.Assigned) {
            task.setStatus(CleaningTask.Status.Accepted); // Automatically accept the task
            cleaningTaskRepository.save(task);
            return true;
        } else {
            throw new RuntimeException("Task not assigned to this worker or task cannot be accepted");
        }
    }

    public void applyLeave(Long workerId, LeaveApplication leaveApplication) {
        Worker worker = workerRepository.findById(workerId)
                .orElseThrow(() -> new RuntimeException("Worker not found"));

        leaveApplication.setWorker(worker);  // Set the worker for the leave application
        leaveApplication.setStatus(LeaveApplication.Status.Pending);  // Initially set status to pending
        leaveApplicationRepository.save(leaveApplication);  // Save the leave application
    }

    public boolean confirmCompletion(int taskId, Long workerId) {
        Optional<CleaningTask> taskOpt = cleaningTaskRepository.findById(taskId);
        Optional<Worker> workerOpt = workerRepository.findById(workerId);

        if (taskOpt.isPresent() && workerOpt.isPresent()) {
            CleaningTask task = taskOpt.get();
            Worker worker = workerOpt.get();

            if (task.getWorker().equals(worker) && task.getStatus() == CleaningTask.Status.Accepted) {
                task.setStatus(CleaningTask.Status.Completed);
                cleaningTaskRepository.save(task);
                return true;
            }
        }
        return false;
    }


    public List<workerDTO> getAllWorkers() {
        List<Worker> workers = workerRepository.findAll();
        return workers.stream()
                .map(worker -> new workerDTO(
                worker.getWorkerId().longValue(),
                worker.getName(),
                worker.getPhoneNumber(),
                worker.getShortBio(),
                worker.isAvailable()
        ))
                .collect(Collectors.toList());
    }

    public List<workerDetailsDTO> getWorkerDetails() {
        List<Worker> workers = workerRepository.findAll();
        return workers.stream()
                .map(worker -> new workerDetailsDTO(
                worker.getWorkerId().longValue(),
                worker.getName(),
                worker.getPhoneNumber(),
                worker.getShortBio(),
                worker.getDeployed(),
                worker.getCurPropertyId(),
                worker.getWorkerHoursInWeek()
        ))
                .collect(Collectors.toList());
    }

    //Long workerId, String name, String phoneNumber, String shortBio, boolean available
    // function that updates the status of a task to progress
    public boolean updateToProgress(int taskId, Long workerId) {
        Optional<CleaningTask> taskOpt = cleaningTaskRepository.findById(taskId);
        Optional<Worker> workerOpt = workerRepository.findById(workerId);

        if (taskOpt.isPresent() && workerOpt.isPresent()) {
            CleaningTask task = taskOpt.get();
            Worker worker = workerOpt.get();
            Property property = task.getProperty();
            Long propId = property.getPropertyId();

            if (task.getWorker().equals(worker) && task.getStatus() == CleaningTask.Status.Accepted) {
                task.setStatus(CleaningTask.Status.InProgress);
                cleaningTaskRepository.save(task);
                worker.setCurPropertyId(propId);
                return true;
            }
        }
        return false;
    }

    public Integer getWorkerIdByUsername(String username) {
        List<Worker> workers = workerRepository.findByName(username);
        if (workers.isEmpty()) {
            throw new RuntimeException("Worker not found with username: " + username);
        }
        Worker worker = workers.get(0);
        return worker.getWorkerId();
    }

    // Method to get adminId based on workerId
    public Long getAdminIdByWorkerId(Long workerId) {
        Optional<Worker> optionalWorker = workerRepository.findById(workerId);

        // Throw RuntimeException if worker is not found
        Worker worker = optionalWorker.orElseThrow(()
                -> new RuntimeException("Worker not found with ID: " + workerId)
        );

        // Return the adminId from the worker
        return worker.getAdminId();
    }

    public List<Worker> getAvailableWorkers() {
        return workerRepository.findAllAvailableWorkers();
    }

    public Integer getWorkerIdByUsername_auth(String username) {
        List<Worker> workers = workerRepository.findByName(username);
        if (workers.isEmpty()) {
            throw new RuntimeException("Worker not found with username: " + username);
        }
        Worker worker = workers.get(0);
        return worker.getWorkerId();
    }
    public boolean updateWorkerHours(Long workerId) {
        // Check if the worker exists
        Optional<Worker> workerOptional = workerRepository.findById(workerId);
        if (!workerOptional.isPresent()) {
            return false; 
        }
        
        System.out.println(workerId);
        Worker worker = workerOptional.get();
    
        // Apply the 4-hour logic to the worker's hours (always add 4 hours)
        Integer newHours = worker.getWorkerHoursInWeek() + 4; 
        worker.setWorkerHoursInWeek(newHours);
    
        // Save the updated worker to the database
        workerRepository.save(worker);
    
        return true; 
    }

}
