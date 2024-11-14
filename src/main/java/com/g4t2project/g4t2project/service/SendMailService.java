package com.g4t2project.g4t2project.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.courier.api.Courier;
import com.courier.api.requests.SendMessageRequest;
import com.courier.api.resources.send.types.MessageRecipient;
import com.courier.api.resources.send.types.Recipient;
import com.courier.api.resources.send.types.TemplateMessage;
import com.courier.api.resources.send.types.UserRecipient;
import com.courier.api.resources.send.types.Message;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class SendMailService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SendMailService.class);

    @Value("${courier.auth.token}")
    private String courierAuthToken;

    @Value("${courier.template.id}")
    private String courierTemplateId;

    @Value("${courier.template.id.reschedule}")
    private String rescheduleTemplateId;

    @Value("${courier.template.id.pendingMc}")
    private String pendingMcTemplateId;

    @Value("${courier.template.id.lateleave}")
    private String lateLeaveTemplateId;

    @Value("${courier.template.id.remindWorker}")
    private String remindWorkerTemplateId;

    @Value("${courier.template.id.alertAdmin}")
    private String alertTemplateId;

    public void sendEnhancedMail(String recipientEmail, String title, String community, String name, String verificationLink) {
        try {
            Courier courier = Courier.builder()
                .authorizationToken(courierAuthToken)
                .build();

            Map<String, Object> data = new HashMap<>();
            data.put("title", title);
            data.put("community", community);
            data.put("name", name);
            data.put("verificationLink", verificationLink);

            SendMessageRequest request = SendMessageRequest.builder()
                .message(Message.of(TemplateMessage.builder()
                    .template(courierTemplateId)
                    .to(MessageRecipient.of(Recipient.of(UserRecipient.builder()
                        .email(recipientEmail)
                        .build())))
                        .data(data)
                    .build()))
                .build();

            courier.send(request);
            LOGGER.info("Enhanced mail sent successfully");
        } catch (Exception e) {
            LOGGER.error("Error while sending the enhanced mail", e);
            e.printStackTrace();
            throw new RuntimeException("Error while sending the enhanced mail: " + e.getMessage());
        }
    }
    public void sendLeaveMail(String recipientEmail, String title, String community, String name, String worker_name) {
        try {
            Courier courier = Courier.builder()
                .authorizationToken(courierAuthToken)
                .build();

            Map<String, Object> data = new HashMap<>();
            data.put("title", title);
            data.put("community", community);
            data.put("name", name);
            data.put("worker_name", worker_name);

            SendMessageRequest request = SendMessageRequest.builder()
                .message(Message.of(TemplateMessage.builder()
                    .template(pendingMcTemplateId)
                    .to(MessageRecipient.of(Recipient.of(UserRecipient.builder()
                        .email(recipientEmail)
                        .build())))
                        .data(data)
                    .build()))
                .build();

            courier.send(request);
            LOGGER.info("Enhanced mail sent successfully");
        } catch (Exception e) {
            LOGGER.error("Error while sending the enhanced mail", e);
            e.printStackTrace();
            throw new RuntimeException("Error while sending the enhanced mail: " + e.getMessage());
        }
    }
    public void sendRescheduleMail(String recipientEmail, String title, String community, String name, String client_name, LocalDate task_date) {
        try {
            Courier courier = Courier.builder()
                .authorizationToken(courierAuthToken)
                .build();

            Map<String, Object> data = new HashMap<>();
            data.put("title", title);
            data.put("community", community);
            data.put("name", name);
            data.put("client_name", client_name);
            data.put("task_date", task_date);

            SendMessageRequest request = SendMessageRequest.builder()
                .message(Message.of(TemplateMessage.builder()
                    .template(rescheduleTemplateId)
                    .to(MessageRecipient.of(Recipient.of(UserRecipient.builder()
                        .email(recipientEmail)
                        .build())))
                        .data(data)
                    .build()))
                .build();

            courier.send(request);
            LOGGER.info("Enhanced mail sent successfully");
        } catch (Exception e) {
            LOGGER.error("Error while sending the enhanced mail", e);
            e.printStackTrace();
            throw new RuntimeException("Error while sending the enhanced mail: " + e.getMessage());
        }
    }
    public void sendLateLeaveEmail(String recipientEmail, String title, String community, String name, LocalDate task_date) {
        try {
            Courier courier = Courier.builder()
                .authorizationToken(courierAuthToken)
                .build();

            Map<String, Object> data = new HashMap<>();
            data.put("title", title);
            data.put("community", community);
            data.put("name", name);
            data.put("task_date", task_date);

            SendMessageRequest request = SendMessageRequest.builder()
                .message(Message.of(TemplateMessage.builder()
                    .template(lateLeaveTemplateId)  // Use the late leave template
                    .to(MessageRecipient.of(Recipient.of(UserRecipient.builder()
                        .email(recipientEmail)
                        .build())))
                        .data(data)
                    .build()))
                .build();

            courier.send(request);
            LOGGER.info("Late leave mail sent successfully to {}", recipientEmail);
        } catch (Exception e) {
            LOGGER.error("Error while sending the late leave mail", e);
            e.printStackTrace();
            throw new RuntimeException("Error while sending the late leave mail: " + e.getMessage());
        }
    }

    public void sendReminderEmail(String recipientEmail, String title, String workerName, String taskDetails, LocalDate taskDate) {
        try {
            Courier courier = Courier.builder()
                .authorizationToken(courierAuthToken)
                .build();
    
            Map<String, Object> data = new HashMap<>();
            data.put("title", title);
            data.put("workerName", workerName);  // Include worker's name in the email
            data.put("taskDetails", taskDetails);  // Add the task details to the email body
            data.put("taskDate", taskDate);  // Include the task date in the email
    
            SendMessageRequest request = SendMessageRequest.builder()
                .message(Message.of(TemplateMessage.builder()
                    .template(remindWorkerTemplateId)  // Use the appropriate template for reminders
                    .to(MessageRecipient.of(Recipient.of(UserRecipient.builder()
                        .email(recipientEmail)
                        .build())))
                    .data(data)
                    .build()))
                .build();
    
            courier.send(request);
            LOGGER.info("Reminder email sent successfully to {}", recipientEmail);
        } catch (Exception e) {
            LOGGER.error("Error while sending the reminder email", e);
            e.printStackTrace();
            throw new RuntimeException("Error while sending the reminder email: " + e.getMessage());
        }
    }

    public void sendAlertEmail(String adminEmail, String detailsOfTask, String workerName, Integer workerId, LocalDate taskDate) {
        try {
            Courier courier = Courier.builder()
                .authorizationToken(courierAuthToken)
                .build();
    
            Map<String, Object> data = new HashMap<>();
            data.put("detailsOfTask", detailsOfTask);  // Include the task details in the email
            data.put("workerName", workerName);  // Add the worker's name to the email body
            data.put("workerId", workerId);  // Include the worker's ID in the email
            data.put("taskDate", taskDate);  // Add the task date to the email body
    
            SendMessageRequest request = SendMessageRequest.builder()
                .message(Message.of(TemplateMessage.builder()
                    .template(alertTemplateId)  // Use the alert template
                    .to(MessageRecipient.of(Recipient.of(UserRecipient.builder()
                        .email(adminEmail)
                        .build())))
                    .data(data)
                    .build()))
                .build();
    
            courier.send(request);
            LOGGER.info("Alert email sent successfully to {}", adminEmail);
        } catch (Exception e) {
            LOGGER.error("Error while sending the alert email", e);
            e.printStackTrace();
            throw new RuntimeException("Error while sending the alert email: " + e.getMessage());
        }
    }
    




}