package com.g4t2project.g4t2project.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.g4t2project.g4t2project.DTO.OverwriteCleaningTaskDTO;
import com.g4t2project.g4t2project.entity.CleaningTask;
import com.g4t2project.g4t2project.entity.LeaveApplication;
import com.g4t2project.g4t2project.entity.Property;
import com.g4t2project.g4t2project.entity.Worker;
import com.g4t2project.g4t2project.exception.NoAvailableWorkerException;
import com.g4t2project.g4t2project.repository.CleaningTaskRepository;
import com.g4t2project.g4t2project.repository.FeedbackRepository;
import com.g4t2project.g4t2project.repository.LeaveApplicationRepository;
import com.g4t2project.g4t2project.repository.PropertyRepository;
import com.g4t2project.g4t2project.repository.FeedbackRepository;
import com.g4t2project.g4t2project.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.g4t2project.g4t2project.util.DistanceCalculator;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.g4t2project.g4t2project.repository.WorkerRepository;

@Service
public class CleaningTaskService {

    @Autowired
    private FeedbackRepository feedbackRepository;
    @Autowired
    private CleaningTaskRepository cleaningTaskRepository;
    @Autowired
    private WorkerRepository workerRepository;
    @Autowired
    private LeaveApplicationRepository leaveApplicationRepository;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private PropertyRepository propertyRepository; 
    @Autowired
    private DistanceCalculator distanceCalculator;

    
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

    public void addCleaningTask(CleaningTask cleaningTask) {
        // Retrieve the Property based on the propertyId from the task
        Property property = cleaningTask.getProperty();
        System.out.println("Property found: " + property);
        // Find the closest worker based on proximity to the property
        Optional<Worker> closestWorkerOpt = findClosestWorker(property, cleaningTask.getDate(), cleaningTask.getShift());

        if (closestWorkerOpt.isPresent()) {
            Worker closestWorker = closestWorkerOpt.get();
            closestWorker.setAvailable(false); // Set the worker as unavailable
            cleaningTask.setWorker(closestWorker); // Assign the worker to the task
            cleaningTask.setStatus(CleaningTask.Status.Assigned); // Set the status
            cleaningTaskRepository.save(cleaningTask); // Save the task
        } else {
            // Handle the case when no worker is available
            throw new NoAvailableWorkerException("No worker available for the task on " + cleaningTask.getDate() + " during " + cleaningTask.getShift()
                    + " shift.");
        }
    }

    public Optional<Worker> findClosestWorker(Property taskProperty, LocalDate taskDate, CleaningTask.Shift taskShift) {
        // Fetch only deployed workers
        List<Worker> deployedWorkers = workerRepository.findAllDeployed(0);
        Worker closestWorker = null;
        double minDistance = Double.MAX_VALUE;

        double taskLat = taskProperty.getLatitude();
        double taskLon = taskProperty.getLongitude();
        System.out.println("Task lat: " + taskLat + " Task lon: " + taskLon);
        System.out.println("_________________________________________________________");

        // First, try to find a deployed worker who is closest and available
        for (Worker worker : deployedWorkers) {
            if (worker.isAvailableOn(taskDate, taskShift)) {  // Ensure the worker is available on the task date and shift
                int curWorkerPropertyId = worker.getCurPropertyId();
                Optional<Property> currentPropertyOpt = propertyRepository.findById((long) curWorkerPropertyId);

                if (currentPropertyOpt.isPresent()) {
                    Property currentProperty = currentPropertyOpt.get();
                    double workerLat = currentProperty.getLatitude();
                    double workerLon = currentProperty.getLongitude();

                    try {
                        double distance = distanceCalculator.calculateDistance(workerLat, workerLon, taskLat, taskLon);  // Use the DistanceCalculator

                        if (distance < minDistance) {
                            minDistance = distance;
                            closestWorker = worker;
                        }
                    } catch (Exception e) {
                        // Handle any error from DistanceCalculator
                        e.printStackTrace();
                    }
        
                   
                } 

                    double distance = calculateDistance(workerLat, workerLon, taskLat, taskLon);

                    if (distance < minDistance) {
                        minDistance = distance;
                        closestWorker = worker;
                    }
                }
            }
        }

        double hqLat = propertyRepository.findById(100L).get().getLatitude(); // Fetch HQ latitude
        double hqLon = propertyRepository.findById(100L).get().getLongitude(); // Fetch HQ latitude;
        System.out.println("HQ lat: " + hqLat + " HQ lon: " + hqLon);
        System.out.println("_________________________________________________________");
        System.out.println("_________________________________________________________");
       
        try {
            double hqToTaskDistance = distanceCalculator.calculateDistance(hqLat, hqLon, taskLat, taskLon);  // Distance from HQ to task

            if (closestWorker == null || minDistance > hqToTaskDistance) {
                List<Worker> hqWorkers = workerRepository.findAllNotDeployed(0);  // Fetch workers assigned to HQ
                if (!hqWorkers.isEmpty()) {
                    closestWorker = hqWorkers.get(0);  // Assign the first worker from HQ
                }
            }
        } catch (Exception e) {
            // Handle any error from DistanceCalculator
            e.printStackTrace();
        }

        return Optional.ofNullable(closestWorker);
    }

    // public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
    //     // using harvesine formula to calculate distance between two points
    //     final int R = 6371; 
    //     double latDistance = Math.toRadians(lat2 - lat1);
    //     double lonDistance = Math.toRadians(lon2 - lon1);
    //     double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
    //             + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
    //             * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
    //     double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    //     return R * c; // Distance in km
    // }
    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // using harvesine formula to calculate distance between two points
        final int R = 6371;
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // Distance in km
    }

    public Property getPropertyById(Long propertyId) {
        return propertyRepository.findById(propertyId).orElse(null);
    }

    public List<CleaningTask> getCleaningTasksByClient(Integer clientId) {
        return cleaningTaskRepository.findTasksByClient(clientId);
    }

    public List<OverwriteCleaningTaskDTO> getAllCleaningTasks() {
        List<CleaningTask> tasks = cleaningTaskRepository.findAll();
        return tasks.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public OverwriteCleaningTaskDTO convertToDTO(CleaningTask task) {
        OverwriteCleaningTaskDTO dto = new OverwriteCleaningTaskDTO();
        dto.setTaskId(task.getTaskId());
        dto.setAcknowledged(task.isAcknowledged());
        dto.setDate(task.getDate());
        dto.setShift(task.getShift().name());
        dto.setStatus(task.getStatus().name());
        dto.setFeedbackId(task.getFeedback() != null ? task.getFeedback().getFeedbackId() : null);
        dto.setPropertyId(task.getProperty().getPropertyId());
        dto.setWorkerId(task.getWorker() != null ? (long) task.getWorker().getWorkerId() : null);
        return dto;
    }

    public CleaningTask updateCleaningTask(OverwriteCleaningTaskDTO taskDTO) {
        CleaningTask existingTask = cleaningTaskRepository.findById(taskDTO.getTaskId())
                .orElseThrow(() -> new RuntimeException("Task not found"));

        existingTask.setAcknowledged(taskDTO.isAcknowledged());
        existingTask.setDate(taskDTO.getDate());
        existingTask.setShift(CleaningTask.Shift.valueOf(taskDTO.getShift()));
        existingTask.setStatus(CleaningTask.Status.valueOf(taskDTO.getStatus()));

        if (taskDTO.getFeedbackId() != null) {
            existingTask.setFeedback(feedbackRepository.findById(taskDTO.getFeedbackId()).orElse(null));
        } else {
            existingTask.setFeedback(null);
        }

        if (taskDTO.getPropertyId() != null) {
            existingTask.setProperty(propertyRepository.findById(taskDTO.getPropertyId())
                    .orElseThrow(() -> new RuntimeException("Property not found")));
        }

        if (taskDTO.getWorkerId() != null) {
            existingTask.setWorker(workerRepository.findById(taskDTO.getWorkerId())
                    .orElseThrow(() -> new RuntimeException("Worker not found")));
        } else {
            existingTask.setWorker(null);
        }

        return cleaningTaskRepository.save(existingTask);
    }

    public CleaningTask assignTaskToWorkerWithConstraints(int taskId, Long workerId, LocalDate taskDate, CleaningTask.Shift shift) {
        CleaningTask task = cleaningTaskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        Worker worker = workerRepository.findById(workerId)
                .orElseThrow(() -> new RuntimeException("Worker not found"));

        // Define shift times
        LocalTime morningStart = LocalTime.of(8, 0);
        LocalTime morningEnd = LocalTime.of(12, 0);
        LocalTime afternoonStart = LocalTime.of(13, 0);
        LocalTime afternoonEnd = LocalTime.of(17, 0);
        LocalTime eveningStart = LocalTime.of(18, 0);
        LocalTime eveningEnd = LocalTime.of(22, 0);

        // Map shift to start and end times
        LocalTime shiftStart;
        LocalTime shiftEnd;

        switch (shift) {
            case Morning -> {
                shiftStart = morningStart;
                shiftEnd = morningEnd;
            }
            case Afternoon -> {
                shiftStart = afternoonStart;
                shiftEnd = afternoonEnd;
            }
            case Evening -> {
                shiftStart = eveningStart;
                shiftEnd = eveningEnd;
            }
            default -> throw new RuntimeException("Invalid shift provided.");
        }

        // Check if assigning the task would exceed the worker's 44-hour weekly limit
        int currentWeeklyHours = worker.getWorkerHoursInWeek(); 
        int shiftDurationHours = (int) Duration.between(shiftStart, shiftEnd).toHours();
        if (currentWeeklyHours + shiftDurationHours > 44) {
            throw new RuntimeException("Assigning this task will exceed the worker's 44-hour weekly limit.");
        }

        // Adjust task assignment time to 2.5 hours before the shift start
        LocalDateTime taskAssignmentTime = LocalDateTime.of(taskDate, shiftStart).minusHours(2).minusMinutes(30);

        // assigning
        task.setWorker(worker);
        task.setStatus(CleaningTask.Status.Assigned);
        task.setShift(shift);
        task.setDate(taskDate);

        // task.setAssignedTime(taskAssignmentTime);

        // worker.setWorkerHoursInWeek(currentWeeklyHours + shiftDurationHours); //only after 'finished'
        // workerRepository.save(worker);

        return cleaningTaskRepository.save(task);
    }


}
