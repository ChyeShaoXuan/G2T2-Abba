package com.g4t2project.g4t2project.service;

import com.g4t2project.g4t2project.entity.CleaningTask;
import com.g4t2project.g4t2project.entity.LeaveApplication;
import com.g4t2project.g4t2project.entity.Worker;
import com.g4t2project.g4t2project.repository.CleaningTaskRepository;
import com.g4t2project.g4t2project.repository.WorkerRepository;
import com.g4t2project.g4t2project.repository.LeaveApplicationRepository;
import com.g4t2project.g4t2project.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        List<CleaningTask> tasks = cleaningTaskRepository.findTasksByWorkerAndDate(worker, leaveApplication.getStartDate());

        for (CleaningTask task : tasks) {
            // try reassign task to another worker
            Optional<Worker> replacementWorker = findReplacementWorker(task);
            if (replacementWorker.isPresent()) {
                task.setWorker(replacementWorker.get());
                cleaningTaskRepository.save(task);
            } else {
                // notify client to reschedule or cancel session
                notificationService.notifyClientForReschedule(task.getProperty().getClient(), task);
            }
        }
    }

    private Optional<Worker> findReplacementWorker(CleaningTask task) {
        // logic to find a replacement worker (e.g., by availability, proximity)
        return workerRepository.findAvailableWorker(task.getDate(), task.getShift());
    }
}

