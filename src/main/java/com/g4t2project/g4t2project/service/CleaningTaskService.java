@Service
public class CleaningTaskService {
    @Autowired
    private CleaningTaskRepository cleaningTaskRepository;
    @Autowired
    private WorkerRepository workerRepository;
    @Autowired
    private LeaveApplicationRepository leaveApplicationRepository;
    @Autowired
    private NotificationService notificationService;

    public void handleWorkerLeave(LeaveApplication leaveApplication) {
        Worker worker = leaveApplication.getWorker();
        List<CleaningTask> tasks = cleaningTaskRepository.findTasksByWorkerAndDate(worker, leaveApplication.getLeaveDate());

        for (CleaningTask task : tasks) {
            // try to reassign the task to another worker
            Optional<Worker> replacementWorker = findReplacementWorker(task);
            if (replacementWorker.isPresent()) {
                task.setWorker(replacementWorker.get());
                cleaningTaskRepository.save(task);
            } else {
                // notify client to reschedule or cancel the session
                notificationService.notifyClientForReschedule(task.getProperty().getClient(), task);
            }
        }
    }

    private Optional<Worker> findReplacementWorker(CleaningTask task) {
        // logic to find a replacement worker (e.g., by availability, proximity)
        return workerRepository.findAvailableWorker(task.getDate(), task.getShift());
    }
}

