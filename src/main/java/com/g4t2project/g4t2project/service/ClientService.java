package com.g4t2project.g4t2project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.g4t2project.g4t2project.repository.*;
import com.g4t2project.g4t2project.DTO.*;
import com.g4t2project.g4t2project.entity.*;
import com.g4t2project.g4t2project.entity.CleaningPackage.PackageType;
import com.g4t2project.g4t2project.entity.CleaningPackage.PropertyType;
import com.g4t2project.g4t2project.exception.ManualBookingRequiredException;

import java.time.*;

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
    private CleaningTaskService cleaningTaskService;

    public ClientDTO convertToClientDTO(Client client) {
        return new ClientDTO(client.getClientId(), client.getEmail(), client.getName(), client.getPhoneNumber());
    }

    public cleaningTaskDTO convertToCleaningTaskDTO(CleaningTask task) {
        Worker worker = task.getWorker();
        workerDTO workerDTO = new workerDTO(Long.valueOf(worker.getWorkerId()), worker.getName(), worker.getPhoneNumber(), worker.getShortBio(),worker.isAvailable());
        // workerDTO workerDTO = new workerDTO(Long.valueOf(task.getWorker().getWorkerId()), task.getWorker().getName(), task.getWorker().getPhoneNumber());
        Long propertyId = task.getProperty().getPropertyId();
        return new cleaningTaskDTO(propertyId, task.getShift().name(), task.getDate().toString(), task.isAcknowledged(), workerDTO);
    }

    public cleaningTaskDTO placeOrder(Long clientId, String packageType, String propertyType, int numberOfRooms, CleaningTask.Shift shift, LocalDate date, Long preferredWorkerId) {

        // Ensure the order is placed one day in advance with at least 24 hours' notice
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime earliestAllowed = currentDateTime.plusHours(24);
        LocalDateTime shiftStartDateTime = date.atTime(getShiftStartTime(shift));
        if (shiftStartDateTime.isBefore(earliestAllowed)) {
            throw new IllegalArgumentException("Orders must be placed at least 24 hours in advance.");
        }
        if (!date.isEqual(LocalDate.now().plusDays(1))) {
            throw new IllegalArgumentException("Orders can only be placed one day in advance.");
        }

        Client client = clientRepository.findById(clientId).orElseThrow(() -> new IllegalArgumentException("Client not found"));
        System.out.println("Client found: " + client.getClientId());

        if (PackageType.valueOf(packageType) == PackageType.Weekly && PropertyType.valueOf(propertyType) == PropertyType.Landed) {
            throw new ManualBookingRequiredException("Please note that HomecleaningSg offers regular cleaning services for landed properties. If you are interested, kindly contact our Sales Representatives at +65 31650568 for more information.");
        }

        CleaningPackage pkg = cleaningPackageRepository.findByPackageTypeAndPropertyType(
            PackageType.valueOf(packageType),
            PropertyType.valueOf(propertyType)
        ).orElseThrow(() -> new IllegalArgumentException("Package not found for type: " + packageType + ", property type: " + propertyType + ", and number of rooms: " + numberOfRooms));
        System.out.println("Package found: " + pkg.getPackageId());

        if (pkg.isManualBookingRequired()) {
            throw new IllegalArgumentException("This package requires manual booking. Please contact the sales team.");
        }

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
        
        CleaningTask cleaningTask = new CleaningTask();
        cleaningTask.setProperty(property);
        cleaningTask.setShift(shift);
        cleaningTask.setStatus(CleaningTask.Status.Scheduled);
        cleaningTask.setDate(date);
        System.out.println("Cleaning task created: " + cleaningTask.getTaskId());

        // Set the preferred worker ID if provided
        if (preferredWorkerId != null) {
            cleaningTask.setPreferredWorkerId(preferredWorkerId);
    }
        cleaningTaskService.addCleaningTask(cleaningTask);
        // try {
        //     cleaningTaskService.addCleaningTask(cleaningTask);
        // } catch (NoAvailableWorkerException e) {
        //     throw new RuntimeException("No worker available for the task: " + e.getMessage());
        // }

        return convertToCleaningTaskDTO(cleaningTask);
    }

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

    public Long getClientIdByName(String clientName) {
        Client client = clientRepository.findByName(clientName)
                .orElseThrow(() -> new RuntimeException("Client not found with name: " + clientName));
        return client.getClientId();
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
