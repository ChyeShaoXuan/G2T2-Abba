package com.g4t2project.g4t2project.service;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.g4t2project.g4t2project.DTO.FeedbackDTO;
import com.g4t2project.g4t2project.DTO.OverwriteCleaningTaskDTO;
import com.g4t2project.g4t2project.entity.CleaningTask;
import com.g4t2project.g4t2project.entity.Feedback;
import com.g4t2project.g4t2project.entity.LeaveApplication;
import com.g4t2project.g4t2project.entity.Property;
import com.g4t2project.g4t2project.entity.Worker;
import com.g4t2project.g4t2project.exception.NoAvailableWorkerException;
import com.g4t2project.g4t2project.repository.CleaningTaskRepository;
import com.g4t2project.g4t2project.repository.FeedbackRepository;
import com.g4t2project.g4t2project.repository.LeaveApplicationRepository;
import com.g4t2project.g4t2project.repository.PropertyRepository;
import com.g4t2project.g4t2project.repository.WorkerRepository;
import com.g4t2project.g4t2project.util.DistanceCalculator;


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
        
        System.out.println("----------------------------------");
        System.out.println("Creating new task......");
        System.out.println("----------------------------------");

        // Find the closest worker based on proximity to the property
        // Optional<Worker> closestWorkerOpt = findClosestWorker(property, cleaningTask.getDate(), cleaningTask.getShift());

        // if (closestWorkerOpt.isPresent()) {
        //     System.out.println(closestWorkerOpt.isPresent());
        //     Worker closestWorker = closestWorkerOpt.get();
        //     cleaningTask.setWorker(closestWorker); // Assign the worker to the task
        //     cleaningTask.setStatus(CleaningTask.Status.Assigned); // Set the status
        //     cleaningTaskRepository.save(cleaningTask); // Save the task
        // } else {
        //     // Handle the case when no worker is available
        //     throw new NoAvailableWorkerException("No worker available for the task on " + cleaningTask.getDate() + " during " + cleaningTask.getShift()
        //             + " shift.");
        // }

        Worker assignedWorker = null;

        // Check if a preferred worker is specified and valid
        if (cleaningTask.getPreferredWorkerId() != null) {
            Long preferredWorkerId = cleaningTask.getPreferredWorkerId();
            
            // Attempt to find the preferred worker
            assignedWorker = workerRepository.findById(preferredWorkerId)
                .orElseThrow(() -> new IllegalArgumentException("Preferred worker not found with ID: " + preferredWorkerId));
            
            // Check if the preferred worker is available on the requested date and shift
            if (!assignedWorker.isAvailableOn(cleaningTask.getDate(), cleaningTask.getShift())) {
                throw new NoAvailableWorkerException("Preferred worker is not available for the task on " 
                    + cleaningTask.getDate() + " during " + cleaningTask.getShift() + " shift.");
            }
    
            System.out.println("Assigned preferred worker: " + assignedWorker.getWorkerId());
        } else {
            // No preferred worker provided, find the closest available worker
            Optional<Worker> closestWorkerOpt = findClosestWorker(cleaningTask.getProperty(), cleaningTask.getDate(), cleaningTask.getShift());
            
            if (closestWorkerOpt.isPresent()) {
                assignedWorker = closestWorkerOpt.get();
                System.out.println("Assigned closest available worker: " + assignedWorker.getWorkerId());
            } else {
                throw new NoAvailableWorkerException("No worker available for the task on " 
                    + cleaningTask.getDate() + " during " + cleaningTask.getShift() + " shift.");
            }
        }
    
        // Final check to ensure assignedWorker is not null
        // if (assignedWorker == null) {
        //     throw new IllegalStateException("Worker assignment failed, no worker was assigned.");
        // }
    
        // Assign the worker and set the task status as Assigned
        cleaningTask.setWorker(assignedWorker);
        cleaningTask.setStatus(CleaningTask.Status.Assigned);
        cleaningTaskRepository.save(cleaningTask);
    
        System.out.println("Cleaning task successfully assigned to worker ID: " + assignedWorker.getWorkerId());
    }

    public Optional<Worker> findClosestWorker(Property taskProperty, LocalDate taskDate, CleaningTask.Shift taskShift) {
        Worker closestWorker = null;
        double minDistance = Double.MAX_VALUE;

        double taskLat = taskProperty.getLatitude();
        double taskLon = taskProperty.getLongitude();
        System.out.println("Task lat: " + taskLat + " Task lon: " + taskLon);

        // Fetch all workers
        List<Worker> allWorkers = workerRepository.findAll();
         for(Worker curWorker: allWorkers){
            System.out.println("Checking worker ID: " + curWorker.getWorkerId());
            if(curWorker.isAvailableOn(taskDate, taskShift)){
                Long curWorkerPropId = (long)curWorker.getCurPropertyId();
                System.out.println("==================================");
                System.out.println("Current worker's property ID: " + curWorkerPropId);
                System.out.println("==================================");

                Optional<Property> curWorkerProperty = propertyRepository.findById(curWorkerPropId);
                // If the property exists, calculate the distance
                System.out.println("----------------------------------");
                System.out.println(curWorkerProperty.isPresent());
                System.out.println("----------------------------------");

                if (curWorkerProperty.isPresent() && check44Hours(curWorker)) {
                    Property property = curWorkerProperty.get();
                    double workerLat = property.getLatitude();
                    double workerLon = property.getLongitude();

                    System.out.println("Calculating distance for curWorkerId: " + curWorker.getWorkerId() + "with name" + curWorker.getName());

                    try {
                        // Calculate the distance between the worker and the task
                        double distance = distanceCalculator.calculateDistance(workerLat, workerLon, taskLat, taskLon);
        
                        // Update closest worker if a closer one is found
                        if (distance < minDistance) {
                            minDistance = distance;
                            closestWorker = curWorker;
                            System.out.println("New closest worker found: " + curWorker.getWorkerId() + " " + closestWorker.getName() + " with distance: " + distance);
                        }
                    } catch (Exception e) {
                        System.err.println("Error calculating distance for worker ID: " + curWorker.getWorkerId());
                        e.printStackTrace();
                    }


                }
            }
        }


        if (closestWorker != null) {
            System.out.println("Closest worker found: Worker ID " + closestWorker.getWorkerId() + " at distance " + minDistance);
        } else {
            System.out.println("No available worker found for the task.");
        }
        
        return Optional.ofNullable(closestWorker);
    }
    
    public Property getPropertyById(Long propertyId) {
        System.out.println("Getting property by id...");
        Property property = propertyRepository.findById(propertyId).orElse(null);
        System.out.println(property);
        System.out.println("Found property!!");
        return property;
    }

    public boolean existsByDateAndShiftAndProperty(LocalDate date, CleaningTask.Shift shift, Property property) {
        Long propId = (long)property.getPropertyId();
        return cleaningTaskRepository.findTaskByDateShiftProperty(propId, date, shift).isPresent();
    }

    public boolean check44Hours(Worker worker) {
        System.out.println("Checking 44 hours for worker ID: " + worker.getWorkerId());
        Integer hours = worker.getWorkerHoursInWeek();
        if (hours == null) {
            return true;
        }
        if(hours + 4 < 44) {
            return true;
        }
        return false;
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
        dto.setPropertyAddress(task.getProperty().getAddress());
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

    public boolean check44h_Requirement(Integer workerId, LocalDate taskDate, CleaningTask.Shift shift) {
        // CleaningTask task = cleaningTaskRepository.findById(taskId)
        //         .orElseThrow(() -> new RuntimeException("Task not found"));
        Worker worker = workerRepository.findById(workerId.longValue())
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
            return false;
            // throw new RuntimeException("Assigning this task will exceed the worker's 44-hour weekly limit.");
        }

        return true;

    }
    
    public void confirmArrival(Integer taskId, MultipartFile photo) throws IOException {
        Optional<CleaningTask> taskOpt = cleaningTaskRepository.findById(taskId);
        if (taskOpt.isPresent()) {
            CleaningTask task = taskOpt.get();
            byte[] photoBytes = photo.getBytes();
            task.confirmArrival(photoBytes);
            cleaningTaskRepository.save(task);
        } else {
            throw new RuntimeException("Task not found");
        }
    }

    public void confirmCompletion(Integer taskId, MultipartFile photo) throws IOException {
        Optional<CleaningTask> taskOpt = cleaningTaskRepository.findById(taskId);
        if (taskOpt.isPresent()) {
            CleaningTask task = taskOpt.get();
            byte[] photoBytes = photo.getBytes();
            task.confirmCompletion(photoBytes);
            cleaningTaskRepository.save(task);
        } else {
            throw new RuntimeException("Task not found");
        }
    }

    public CleaningTask getCleaningTaskById(Integer taskId) {
        return cleaningTaskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }

    public List<OverwriteCleaningTaskDTO> getCleaningTasksById(Integer workerId) {
        List<CleaningTask> workerTasks = cleaningTaskRepository.findTasksByWorker(workerId);
        System.out.println("----------------------------------");
        System.out.println("Worker's cleaning tasks: ");
        System.out.println(workerTasks);
        return workerTasks.stream()
        .map(this::convertToDTO)
        .collect(Collectors.toList());
    }

    public List<FeedbackDTO> getCompletedCleaningTaskByWorker(Integer workerId) {
        List<CleaningTask> completedTasks = cleaningTaskRepository.findCompletedTasksByWorker(workerId);
        return completedTasks.stream()
                .map(task -> new FeedbackDTO(
                        task.getFeedback().getFeedbackId(),
                        task.getFeedback().getRating(),
                        task.getFeedback().getComment()
                ))
                .collect(Collectors.toList());
    }
    
    public List<OverwriteCleaningTaskDTO> getCompletedCleaningTasksByClient(Integer clientId) {
        List<CleaningTask> clientTasks = cleaningTaskRepository.findCompletedTasksByClient(clientId);
        // System.out.println("----------------------------------");
        // System.out.println("Client's cleaning tasks: ");
        // System.out.println(clientTasks);
        return clientTasks.stream()
        .map(this::convertToDTO)
        .collect(Collectors.toList());    
    }

    public void addFeedbackToTask(Integer taskId, FeedbackDTO feedbackDTO) {
        CleaningTask task = cleaningTaskRepository.findById(taskId)
        .orElseThrow(() -> new RuntimeException("Task not found with ID: " + taskId));

        Feedback feedback = new Feedback(feedbackDTO.getRating(), feedbackDTO.getComment(), task);
        feedbackRepository.save(feedback);

        task.setFeedback(feedback);
        cleaningTaskRepository.save(task);
    }


}

