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

import java.util.HashMap;
import java.util.Map;

@Service
public class SendMailService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SendMailService.class);

    @Value("${courier.auth.token}")
    private String courierAuthToken;

    @Value("${courier.template.id}")
    private String courierTemplateId;

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
}