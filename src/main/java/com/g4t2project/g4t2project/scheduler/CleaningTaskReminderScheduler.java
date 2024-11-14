package com.g4t2project.g4t2project.scheduler;

import com.g4t2project.g4t2project.entity.CleaningTask;
import com.g4t2project.g4t2project.service.NotificationService;
import com.g4t2project.g4t2project.repository.CleaningTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class CleaningTaskReminderScheduler {

    @Autowired
    private CleaningTaskRepository cleaningTaskRepository;

    @Autowired
    private NotificationService notificationService;

    // Scheduler to send reminders every 5 mis to workers with pending arrival confirmation
    @Scheduled(fixedRate = 300000)  // 5 mins interval
    public void sendArrivalReminders() {
        // Fetch all tasks assigned to workers where arrival is not confirmed
        List<CleaningTask> assignedTasks = cleaningTaskRepository.findTasksWithoutArrivalConfirmation(CleaningTask.Status.Assigned);

        for (CleaningTask task : assignedTasks) {
            // Send reminder message to the assigned worker
             // Get worker details
            String workerEmail = task.getWorker().getEmailId();
            String workerName = task.getWorker().getName();
            String taskDescription = "Assigned Task";
            String taskDetails = task.getDescription(); 
            LocalDate taskDate = task.getDate(); 

            // Log the reminder
            System.out.println("-------------------------------");
            System.out.println("Sending reminder to worker " + workerName);
            System.out.println("-------------------------------");

            // Send reminder email using NotificationService
            notificationService.notifyWorkerForTaskReminder(workerEmail, 
                    "Task Reminder", 
                    workerName, 
                    taskDetails, 
                    taskDate);
        }
    }
}
