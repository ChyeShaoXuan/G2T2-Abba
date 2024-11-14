package com.g4t2project.g4t2project.scheduler;

import java.sql.ClientInfoStatus;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.g4t2project.g4t2project.entity.CleaningTask;
import com.g4t2project.g4t2project.repository.CleaningTaskRepository;
import com.g4t2project.g4t2project.service.NotificationService;

@Component
public class CheckTaskAcknowledgementScheduler {

    @Autowired
    private CleaningTaskRepository cleaningTaskRepository;

    @Autowired
    private NotificationService notificationService;


    @Scheduled(cron = "0 0/1 * * * ?")  // Every minute
    public void checkWorkerAcknowledgments() {
        System.out.println("Checking worker acknowledgments...");
        // Fetch all tasks assigned to workers where arrival is not confirmed
        List<CleaningTask> assignedTasks = cleaningTaskRepository.findTasksWithoutArrivalConfirmation(CleaningTask.Status.Assigned);
        
        for (CleaningTask task : assignedTasks) {
            // Determine the shift start time based on the worker's shift (Morning, Afternoon, or Night)
            CleaningTask.Shift shift = task.getShift();

            LocalDateTime shiftStartTime = null; // Declare the shift start time variable outside the if/else blocks

            if (shift == CleaningTask.Shift.Morning) {
                // Morning shift starts at 8:00 AM
                shiftStartTime = task.getDate().atTime(8, 0);  // Task date + 8:00 AM
            } else if (shift == CleaningTask.Shift.Afternoon) {
                // Afternoon shift starts at 1:00 PM
                shiftStartTime = task.getDate().atTime(13, 0); // Task date + 1:00 PM
            } else if (shift == CleaningTask.Shift.Evening) {
                // Night shift starts at 6:00 PM
                shiftStartTime = task.getDate().atTime(18, 0); // Task date + 6:00 PM
            }

            LocalDateTime currentTime = LocalDateTime.now();
            if (shiftStartTime != null && currentTime.isAfter(shiftStartTime.plusMinutes(5))) {
                System.out.println("Task " + task.getTaskId() + " is overdue for acknowledgment");
                // Send alert if the acknowledgment time is past
                sendAlertToAdmin(task);
            }
        }
    }
    
    public void sendAlertToAdmin(CleaningTask task) {

        
        // Get admin email
        String adminEmail = task.getWorker().getAdmin().getEmailId(); // Admin email address
        System.out.println("-------------------------------");
        System.out.println("Sending alert to admin " + adminEmail);
        System.out.println("-------------------------------");
        try{
            // Send alert email using NotificationService
            notificationService.alertAdminOfFailedAck(task, adminEmail);
            task.setStatus(CleaningTask.Status.Unacknowledged);
            cleaningTaskRepository.save(task);
        }catch(Exception e){
            System.out.println("Failed to send alert to admin " + adminEmail);
        }
    }


}
