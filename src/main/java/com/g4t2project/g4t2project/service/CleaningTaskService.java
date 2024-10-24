package com.g4t2project.g4t2project.service;

import com.g4t2project.g4t2project.entity.CleaningTask;
import com.g4t2project.g4t2project.entity.LeaveApplication;
import com.g4t2project.g4t2project.entity.Worker;
import com.g4t2project.g4t2project.entity.Property;

import com.g4t2project.g4t2project.repository.CleaningTaskRepository;
import com.g4t2project.g4t2project.repository.WorkerRepository;
import com.g4t2project.g4t2project.repository.LeaveApplicationRepository;
import com.g4t2project.g4t2project.repository.PropertyRepository;
import com.g4t2project.g4t2project.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    @Autowired
    private PropertyRepository propertyRepository; 

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
            throw new RuntimeException("No worker available for the task.");
        }
    }


    private Optional<Worker> findClosestWorker(Property taskProperty, LocalDate taskDate, CleaningTask.Shift taskShift) {
        // Fetch only deployed workers
        List<Worker> deployedWorkers = workerRepository.findAllDeployed(0); 
        Worker closestWorker = null;
        double minDistance = Double.MAX_VALUE;
        
        double taskLat = taskProperty.getLatitude();
        double taskLon = taskProperty.getLongitude();
        
        // First, try to find a deployed worker who is closest and available
        for (Worker worker : deployedWorkers) {
            if (worker.isAvailableOn(taskDate, taskShift)) {  // Ensure the worker is available on the task date and shift
                int curWorkerPropertyId = worker.getCurPropertyId();
                Optional<Property> currentPropertyOpt = propertyRepository.findById(curWorkerPropertyId);
        
                if (currentPropertyOpt.isPresent()) {
                    Property currentProperty = currentPropertyOpt.get();
                    double workerLat = currentProperty.getLatitude();
                    double workerLon = currentProperty.getLongitude();
        
                    double distance = calculateDistance(workerLat, workerLon, taskLat, taskLon);
        
                    if (distance < minDistance) {
                        minDistance = distance;
                        closestWorker = worker;
                    }
                } 
            }
        }

        double hqLat = propertyRepository.findById(0).get().getLatitude(); // Fetch HQ latitude
        double hqLon = propertyRepository.findById(0).get().getLongitude(); // Fetch HQ latitude;
        double hqToTaskDistance = calculateDistance(hqLat, hqLon, taskLat, taskLon); // Distance from HQ to task
    
        // If no deployed worker was found, assign a worker from HQ
        if (closestWorker == null || minDistance > hqToTaskDistance) { 
            List<Worker> hqWorkers = workerRepository.findAllNotDeployed(0); // Fetch workers assigned to HQ
            if (!hqWorkers.isEmpty()) {
                closestWorker = hqWorkers.get(0); // Assign the first worker from HQ since they are available
            }
        }

    
        return Optional.ofNullable(closestWorker);
    }
    

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
    
    private double[] getWorkerCurrentLocation(Worker worker, LocalDate date, CleaningTask.Shift shift) {
        // Fetch the last task assigned to the worker before the given date and shift
        Optional<CleaningTask> lastTaskOpt = cleaningTaskRepository.findLastTaskByWorkerBeforeShift(worker.getWorkerId(), date, shift);
    
        if (lastTaskOpt.isPresent()) {
            CleaningTask lastTask = lastTaskOpt.get();
            Property lastProperty = lastTask.getProperty();
            return new double[] { lastProperty.getLatitude(), lastProperty.getLongitude() };
        } else {
            // If no previous task, use the worker's current property
            if (worker.getCurPropertyId() != 0) {
                Optional<Property> currentPropertyOpt = propertyRepository.findById(worker.getCurPropertyId());
                if (currentPropertyOpt.isPresent()) {
                    Property currentProperty = currentPropertyOpt.get();
                    return new double[] { currentProperty.getLatitude(), currentProperty.getLongitude() };
                }
            }
            // Default to HQ location
            Optional<Property> hqPropertyOpt = propertyRepository.findById(0); // Assuming HQ property has ID 0
            if (hqPropertyOpt.isPresent()) {
                Property hqProperty = hqPropertyOpt.get();
                return new double[] { hqProperty.getLatitude(), hqProperty.getLongitude() };
            } else {
                throw new IllegalStateException("Cannot determine worker's current location.");
            }
        }
    }
    

}

