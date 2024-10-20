@Service
public class NotificationService {
    public void notifyClientForReschedule(Client client, CleaningTask task) {
        // notify client about rescheduling or cancellation
        String message = "Dear " + client.getName() + ", your cleaning session on " + task.getDate() + " has been affected. Please reschedule or cancel.";
        sendEmail(client.getEmail(), message);
    }

    public void notifyAdminForPendingMC(LeaveApplication leaveApplication) {
        // notify admin if MC document is not uploaded by the end of the day
        String message = "The worker " + leaveApplication.getWorker().getName() + " has not yet uploaded their MC slip for leave on " + leaveApplication.getLeaveDate();
        sendEmail("admin@company.com", message);
    }

    private void sendEmail(String to, String message) {
        // send email logic
    }
}

