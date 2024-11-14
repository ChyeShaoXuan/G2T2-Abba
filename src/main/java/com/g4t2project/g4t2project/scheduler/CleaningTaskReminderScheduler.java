package com.g4t2project.g4t2project.scheduler;

import com.g4t2project.g4t2project.entity.CleaningTask;
import com.g4t2project.g4t2project.service.NotificationService;
import com.g4t2project.g4t2project.repository.CleaningTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CleaningTaskReminderScheduler {

    @Autowired
    private CleaningTaskRepository cleaningTaskRepository;

    @Autowired
    private NotificationService notificationService;

    // Scheduler to send reminders every 5 seconds to workers with pending arrival confirmation
    @Scheduled(fixedRate = 5000)  // 5 seconds interval
    public void sendArrivalReminders() {
        // Fetch all tasks assigned to workers where arrival is not confirmed
        List<CleaningTask> assignedTasks = cleaningTaskRepository.findTasksWithoutArrivalConfirmation(CleaningTask.Status.Assigned);

        for (CleaningTask task : assignedTasks) {
            // Send reminder message to the assigned worker
            System.out.println("-------------------------------");
            System.out.println("Sending reminder to worker " + task.getWorker().getName());
            System.out.println("-------------------------------");

        }
    }
}
