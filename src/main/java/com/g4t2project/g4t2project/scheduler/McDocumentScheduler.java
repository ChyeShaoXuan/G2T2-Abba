package com.g4t2project.g4t2project.scheduler;

import com.g4t2project.g4t2project.entity.LeaveApplication;
import com.g4t2project.g4t2project.repository.LeaveApplicationRepository;
import com.g4t2project.g4t2project.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class McDocumentScheduler {

    @Autowired
    private LeaveApplicationRepository leaveApplicationRepository;

    @Autowired
    private NotificationService notificationService;

    // scheduled task to run at 11 PM daily
    @Scheduled(cron = "0 0 23 * * ?")
    public void checkPendingMcSubmissions() {
        // fetch all leave applications with missing MC documents
        List<LeaveApplication> pendingMcSubmissions = leaveApplicationRepository.findPendingMcSubmissions();
        for (LeaveApplication leaveApplication : pendingMcSubmissions) {
            // notify admin about missing MC documents
            notificationService.notifyAdminForPendingMC(leaveApplication);
        }
    }
}

