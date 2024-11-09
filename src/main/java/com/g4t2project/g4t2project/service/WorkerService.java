package com.g4t2project.g4t2project.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.g4t2project.g4t2project.entity.CleaningTask;
import com.g4t2project.g4t2project.entity.LeaveApplication;
import com.g4t2project.g4t2project.entity.Worker;
import com.g4t2project.g4t2project.repository.CleaningTaskRepository;
import com.g4t2project.g4t2project.repository.LeaveApplicationRepository;
import com.g4t2project.g4t2project.repository.WorkerRepository;

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
        return worker != null && worker.getTele_Id().equals(password); 
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

    public List<Long> getAllWorkerIds() {
        return workerRepository.findAllWorkerIds();
    }

    //reset workerhoursweekly and add weekly hours to worker_hours_stats table: totalHours
    @Scheduled(cron = "0 0 0 * * MON") // Runs at midnight every Monday
    public void resetWorkerHoursWeekly() {
        List<Worker> workers = workerRepository.findAll();
        for (Worker worker : workers) {
            //if add first, then reset

            // Reset hours for the new week
            worker.setWorkerHoursInWeek(0);
        }
        workerRepository.saveAll(workers);  // Save updated worker hours
    }

}
