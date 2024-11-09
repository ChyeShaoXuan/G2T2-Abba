package com.g4t2project.g4t2project.service;

import com.g4t2project.g4t2project.entity.CleaningTask;
import com.g4t2project.g4t2project.entity.Client;
import com.g4t2project.g4t2project.entity.LeaveApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class NotificationService {
    private final SendMailService sendMailService;
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationService.class);

    @Value("${courier.auth.token}")
    private String courierAuthToken;

    public NotificationService(SendMailService sendMailService) {
        this.sendMailService = sendMailService;
    }

    // Notify the client to reschedule due to worker unavailability
    public void notifyClientForReschedule(Client client, CleaningTask task) {
        Map<String, Object> data = new HashMap<>();
        data.put("client_name", client.getName());
        data.put("task_date", task.getDate());

        sendMailService.sendRescheduleMail(client.getEmail(), "Reschedule Notice", "Your Community", 
                client.getName(), client.getName(), task.getDate());

        LOGGER.info("Reschedule notification sent to client: {}", client.getEmail());
    }

    // Notify the admin that a worker's leave application is pending approval with MC slip required
    public void notifyAdminForPendingMC(LeaveApplication leaveApplication) {
        Map<String, Object> data = new HashMap<>();
        data.put("worker_name", leaveApplication.getWorker().getName());
        data.put("start_date", leaveApplication.getStartDate());
        data.put("end_date", leaveApplication.getEndDate());

        sendMailService.sendLeaveMail("admin@company.com", "Pending MC for Leave", "Your Community", leaveApplication.getAdmin().getName(),
                leaveApplication.getWorker().getName());

        LOGGER.info("Pending MC notification sent to admin for worker: {}", leaveApplication.getWorker().getName());
    }

    // Notify the client when it's too late to reschedule a service due to a late leave application
    public void notifyClientForLateLeave(Client client, CleaningTask task) {
        Map<String, Object> data = new HashMap<>();
        data.put("client_name", client.getName());
        data.put("task_date", task.getDate());

        // Send email notification for late leave
        sendMailService.sendLateLeaveEmail(client.getEmail(), "Cancelation or Reschedule Appointment", "Your Community", client.getName(), task.getDate() );

        LOGGER.info("Late leave notification sent to client: {}", client.getEmail());

    }
}

