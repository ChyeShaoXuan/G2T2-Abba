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

@Service
public class NotificationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationService.class);

    @Value("${courier.auth.token}")
    private String courierAuthToken;

    @Value("${courier.template.id.reschedule}")
    private String rescheduleTemplateId;

    @Value("${courier.template.id.pendingMc}")
    private String pendingMcTemplateId;

    public void notifyClientForReschedule(Client client, CleaningTask task) {
        Map<String, Object> data = new HashMap<>();
        data.put("client_name", client.getName());
        data.put("task_date", task.getDate());

        sendEmailWithTemplate(client.getEmail(), rescheduleTemplateId, data);
    }

    public void notifyAdminForPendingMC(LeaveApplication leaveApplication) {
        Map<String, Object> data = new HashMap<>();
        data.put("worker_name", leaveApplication.getWorker().getName());
        data.put("start_date", leaveApplication.getStartDate());
        data.put("end_date", leaveApplication.getEndDate());

        sendEmailWithTemplate("admin@company.com", pendingMcTemplateId, data);
    }

    private void sendEmailWithTemplate(String recipientEmail, String templateId, Map<String, Object> data) {
        try {
            Courier courier = Courier.builder()
                .authorizationToken(courierAuthToken)
                .build();

            SendMessageRequest request = SendMessageRequest.builder()
                .message(com.courier.api.resources.send.types.Message.of(
                    TemplateMessage.builder()
                        .template(templateId)
                        .to(MessageRecipient.of(Recipient.of(
                            UserRecipient.builder()
                                .email(recipientEmail)
                                .build())))
                        .data(data)
                        .build()))
                .build();

            courier.send(request);
            LOGGER.info("Email sent successfully to: {}", recipientEmail);
        } catch (Exception e) {
            LOGGER.error("Error while sending email to {}: {}", recipientEmail, e.getMessage());
            throw new RuntimeException("Error while sending email: " + e.getMessage());
        }
    }
}
