package com.g4t2project.g4t2project.service;

import com.g4t2project.g4t2project.entity.CleaningTask;
import com.g4t2project.g4t2project.entity.Client;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    public void notifyClientForReschedule(Client client, CleaningTask task) {
        String message = "Dear " + client.getName() + ", your cleaning session on " + task.getDate() + " has been affected. Please reschedule or cancel.";
        sendEmail(client.getEmail(), message);
    }

    public void notifyAdminForPendingMC(LeaveApplication leaveApplication) {
        String message = "Worker " + leaveApplication.getWorker().getName() + " has not uploaded their MC slip for leave on " + leaveApplication.getLeaveDate();
        sendEmail("admin@company.com", message);
    }

    private void sendEmail(String to, String message) {
        // placeholder for actual email sending logic (Not done yet)
        System.out.println("Sending email to: " + to + "\nMessage: " + message);
    }
}


