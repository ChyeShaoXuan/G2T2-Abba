package com.g4t2project.g4t2project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.g4t2project.g4t2project.repository.*;
import com.g4t2project.g4t2project.DTO.*;
import com.g4t2project.g4t2project.entity.*;
import com.g4t2project.g4t2project.entity.CleaningPackage.PackageType;
import com.g4t2project.g4t2project.entity.CleaningPackage.PropertyType;
import com.g4t2project.g4t2project.exception.NoAvailableWorkerException;

import java.time.*;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private CleaningPackageRepository cleaningPackageRepository;

    @Autowired
    private CleaningTaskRepository cleaningTaskRepository;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private WorkerRepository workerRepository;

    @Autowired
    private CleaningTaskService cleaningTaskService;

    public ClientDTO convertToClientDTO(Client client) {
        return new ClientDTO(client.getClientId(), client.getEmail(), client.getName(), client.getPhoneNumber());
    }

    public PropertyDTO convertToPropertyDTO(Property property) {
        return new PropertyDTO(property.getPropertyId(), property.getNumberOfRooms(), property.getAddress(), property.getLatitude(), property.getLongitude());
    }

    public cleaningTaskDTO convertToCleaningTaskDTO(CleaningTask task) {
        Worker worker = task.getWorker();
        workerDTO workerDTO = new workerDTO(Long.valueOf(worker.getWorkerId()), worker.getName(), worker.getPhoneNumber(), worker.getShortBio(), worker.isAvailable());
        // workerDTO workerDTO = new workerDTO(Long.valueOf(task.getWorker().getWorkerId()), task.getWorker().getName(), task.getWorker().getPhoneNumber());
        Long propertyId = task.getProperty().getPropertyId();
        return new cleaningTaskDTO(propertyId, task.getShift().name(), task.getDate().toString(), task.isAcknowledged(), workerDTO);
    }

    public cleaningTaskDTO placeOrder(Long clientId, String packageType, String propertyType, int numberOfRooms, CleaningTask.Shift shift, LocalDate date) {

        // Enforce booking constraints
        // LocalDateTime currentDateTime = LocalDateTime.now();
        // LocalDateTime earliestAllowed = currentDateTime.plusHours(24);
        // LocalDateTime shiftStartDateTime = date.atTime(getShiftStartTime(shift));
        // if (shiftStartDateTime.isBefore(earliestAllowed)) {
        //     throw new IllegalArgumentException("Orders must be placed at least 24 hours in advance.");
        // }
        // if (!date.isEqual(LocalDate.now().plusDays(1))) {
        //     throw new IllegalArgumentException("Orders can only be placed one day in advance.");
        // }

        Client client = clientRepository.findById(clientId).orElseThrow(() -> new IllegalArgumentException("Client not found"));
        System.out.println("Client found: " + client.getClientId());

        CleaningPackage pkg = cleaningPackageRepository.findByPackageTypeAndPropertyTypeAndPax(
            PackageType.valueOf(packageType),
            PropertyType.valueOf(propertyType),
            numberOfRooms
        ).orElseThrow(() -> new IllegalArgumentException("Package not found for type: " + packageType + ", property type: " + propertyType + ", and number of rooms: " + numberOfRooms));
        System.out.println("Package found: " + pkg.getPackageId());

        // Property property = propertyRepository.findByClientAndNumberOfRooms(client, numberOfRooms)
        // .orElseGet(() -> {
        //     Property newProperty = new Property(client, pkg, "", 0.0, 0.0);
        //     newProperty.setNumberOfRooms(numberOfRooms);
        //     return propertyRepository.save(newProperty);
        // });

        Property property = propertyRepository.findByClientAndCleaningPackage(client, pkg)
        .orElseGet(() -> {
            Property newProperty = new Property(client, pkg, "", 0.0, 0.0);
            newProperty.setNumberOfRooms(numberOfRooms);
            return propertyRepository.save(newProperty);
        });
        System.out.println("Property found: " + property.getPropertyId());

        if (shift == null) {
            throw new IllegalArgumentException("Shift is required and cannot be empty");
        }

        // Worker worker = assignWorker(property, shift, date);

        // CleaningTask cleaningTask = new CleaningTask(property, worker, shift, CleaningTask.Status.Scheduled, date, false);

        // CleaningTask savedTask = cleaningTaskRepository.save(cleaningTask);

        CleaningTask cleaningTask = new CleaningTask();
        cleaningTask.setProperty(property);
        cleaningTask.setShift(shift);
        cleaningTask.setStatus(CleaningTask.Status.Scheduled);
        cleaningTask.setDate(date);
        System.out.println("Cleaning task created: " + cleaningTask.getTaskId());
        try {
            cleaningTaskService.addCleaningTask(cleaningTask);
        } catch (NoAvailableWorkerException e) {
            throw new RuntimeException("No worker available for the task: " + e.getMessage());
        }

        return convertToCleaningTaskDTO(cleaningTask);
    }

    // private Worker assignWorker(Property property, CleaningTask.Shift shift, LocalDate date) {
    //     Optional<Worker> optionalWorker = workerRepository.findFirstByAvailableTrue();
    //     return optionalWorker.orElseThrow(() -> new IllegalStateException("No available workers"));
    // }


    public Feedback rateSession(Long clientId, int taskID, int rating, String comments) {
        CleaningTask task = cleaningTaskRepository.findById(taskID).orElseThrow(() -> new IllegalArgumentException("Cleaning task not found"));

        if (!task.getProperty().getClient().getClientId().equals(clientId)) {
            throw new IllegalArgumentException("This task does not belong to the client");
        }
        if (task.getStatus() != CleaningTask.Status.Completed) {
            throw new IllegalStateException("Cannot rate a task that is not completed");
        }

        Feedback feedback = new Feedback(rating, comments, task);
        feedbackRepository.save(feedback);

        task.setFeedback(feedback);
        cleaningTaskRepository.save(task);

        return feedback;
    }

    public Property addProperty(Long clientId,  String address, Long packageID, double latitude, double longitude) {
        Client client = clientRepository.findById(clientId).orElseThrow(() -> new IllegalArgumentException("Client not found"));

        CleaningPackage pkg = cleaningPackageRepository.findById(packageID).orElseThrow(() -> new IllegalArgumentException("Package not found"));
        
        Property property = new Property(client, pkg, address, latitude, longitude);

        return propertyRepository.save(property);
    }

    
    public boolean modifyProperty(Long clientId, Long propertyID, String newAddress) {
        Property property = propertyRepository.findById(propertyID).orElseThrow(() -> new IllegalArgumentException("Property not found"));

        // Check if the property belongs to the client
        if (!property.getClient().getClientId().equals(clientId)) {
            throw new IllegalArgumentException("This property does not belong to the client");
        }

        // Update property details
        property.setAddress(newAddress);
        // property.setPostalCode(newPostalCode);

        propertyRepository.save(property);
        return true;
    }

    public boolean submitFeedback(Long clientId, int taskID, int rating, String comments) {
        try {
            rateSession(clientId, taskID, rating, comments);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public CleaningPackage selectPackage(Long clientId, Long propertyID, Long packageID) {
        Client client = clientRepository.findById(clientId).orElseThrow(() -> new IllegalArgumentException("Client not found"));
        Property property = propertyRepository.findById(propertyID).orElseThrow(() -> new IllegalArgumentException("Property not found"));
        CleaningPackage selectedPackage = cleaningPackageRepository.findById(packageID).orElseThrow(() -> new IllegalArgumentException("Package not found"));
    
        if (!property.getClient().getClientId().equals(clientId)) {
            throw new IllegalArgumentException("This property does not belong to the client");
        }
    
        property.setPkg(selectedPackage);
        propertyRepository.save(property);
    
        return selectedPackage;
    }

    private LocalTime getShiftStartTime(CleaningTask.Shift shift) {
        switch (shift) {
            case Morning:
                return LocalTime.of(8, 0);
            case Afternoon:
                return LocalTime.of(13, 0);
            case Evening:
                return LocalTime.of(18, 0);
            default:
                throw new IllegalArgumentException("Invalid shift: " + shift);
        }
    }
}
