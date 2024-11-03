package com.g4t2project.g4t2project.service;

import com.courier.api.Courier;
import com.courier.api.requests.SendMessageRequest;
import com.courier.api.resources.send.types.MessageRecipient;
import com.courier.api.resources.send.types.Recipient;
import com.courier.api.resources.send.types.TemplateMessage;
import com.courier.api.resources.send.types.UserRecipient;
import com.g4t2project.g4t2project.entity.CleaningTask;
import com.g4t2project.g4t2project.entity.Client;
import com.g4t2project.g4t2project.entity.LeaveApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import com.g4t2project.g4t2project.service.SendMailService;

@Service
public class NotificationService {
    private SendMailService sendMailService;

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationService.class);

    @Value("${courier.auth.token}")
    private String courierAuthToken;

    public void notifyClientForReschedule(Client client, CleaningTask task) {
        Map<String, Object> data = new HashMap<>();
        data.put("client_name", client.getName());
        data.put("task_date", task.getDate());

        sendMailService.sendRescheduleMail(client.getEmail(), "title","Your Community", "Recipient Name", client.getName(), task.getDate());
    }

    public void notifyAdminForPendingMC(LeaveApplication leaveApplication) {
        Map<String, Object> data = new HashMap<>();
        data.put("worker_name", leaveApplication.getWorker().getName());
        data.put("start_date", leaveApplication.getStartDate());
        data.put("end_date", leaveApplication.getEndDate());

        sendMailService.sendLeaveMail("admin@company.com", "title","Your Community", "Recipient Name", leaveApplication.getWorker().getName());
    }
}
