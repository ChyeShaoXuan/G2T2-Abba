package com.g4t2project.g4t2project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.g4t2project.g4t2project.repository.*;
import com.g4t2project.g4t2project.entity.*;

import java.time.LocalDate;
import java.util.Optional;

@Service
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

    public CleaningTask placeOrder(Long clientId, int packageID, int propertyID, CleaningTask.Shift shift, LocalDate date) {
        Client client = clientRepository.findById(clientId).orElseThrow(() -> new IllegalArgumentException("Client not found"));
        Property property = propertyRepository.findById(propertyID).orElseThrow(() -> new IllegalArgumentException("Property not found"));
        CleaningPackage pkg = cleaningPackageRepository.findById(packageID).orElseThrow(() -> new IllegalArgumentException("Package not found"));

        Worker worker = assignWorker(property, shift, date);

        CleaningTask cleaningTask = new CleaningTask(property, worker, shift, CleaningTask.Status.Scheduled, date, false);

        return cleaningTaskRepository.save(cleaningTask);
    }

    private Worker assignWorker(Property property, CleaningTask.Shift shift, LocalDate date) {
        Optional<Worker> optionalWorker = workerRepository.findFirstByAvailableTrue();
        return optionalWorker.orElseThrow(() -> new IllegalStateException("No available workers"));
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

    public Property addProperty(Long clientId,  String address, String postalCode, int packageID, double latitude, double longitude) {
        Client client = clientRepository.findById(clientId).orElseThrow(() -> new IllegalArgumentException("Client not found"));

        CleaningPackage pkg = cleaningPackageRepository.findById(packageID).orElseThrow(() -> new IllegalArgumentException("Package not found"));
        
        Property property = new Property(client, pkg, address, latitude, longitude, postalCode);

        return propertyRepository.save(property);
    }

    
    public boolean modifyProperty(Long clientId, int propertyID, String newAddress, String newPostalCode) {
        Property property = propertyRepository.findById(propertyID).orElseThrow(() -> new IllegalArgumentException("Property not found"));

        // Check if the property belongs to the client
        if (!property.getClient().getClientId().equals(clientId)) {
            throw new IllegalArgumentException("This property does not belong to the client");
        }

        // Update property details
        property.setAddress(newAddress);
        property.setPostalCode(newPostalCode);

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

    public CleaningPackage selectPackage(Long clientId, int propertyID, int packageID) {
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
}
