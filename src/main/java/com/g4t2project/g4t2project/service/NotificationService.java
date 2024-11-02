package com.g4t2project.g4t2project.service;
import com.g4t2project.g4t2project.entity.CleaningTask;
import com.g4t2project.g4t2project.entity.Client;
import com.g4t2project.g4t2project.entity.LeaveApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private final JavaMailSender mailSender;
    @Autowired
    public NotificationService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    public void notifyClientForReschedule(Client client, CleaningTask task) {
        String message = "Dear " + client.getName() + ", your cleaning session on " + task.getDate() + " has been affected. Please reschedule or cancel.";
        sendEmail(client.getEmail(), message);
        String subject = "Reschedule Required for Your Cleaning Session";
        sendEmail(client.getEmail(), subject, message);
    }

    public void notifyAdminForPendingMC(LeaveApplication leaveApplication) {
        String message = "Worker " + leaveApplication.getWorker().getName() + " has not uploaded their MC slip for leave on " + leaveApplication.getStartDate() + " to " + leaveApplication.getEndDate() + ".";
        sendEmail("admin@company.com", message);
        String subject = "Pending MC Slip Upload Notification";
        sendEmail("admin@company.com", subject, message);
    }

    private void sendEmail(String to, String message) {
        // placeholder for actual email sending logic (Not done yet)
        System.out.println("Sending email to: " + to + "\nMessage: " + message);
    }
    private void sendEmail(String to, String subject, String message) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(to);
        email.setSubject(subject);
        email.setText(message);
        mailSender.send(email);
        System.out.println("Email sent to: " + to + "\nSubject: " + subject + "\nMessage: " + message);
    }
}



