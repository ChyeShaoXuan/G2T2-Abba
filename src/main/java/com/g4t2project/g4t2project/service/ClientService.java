package com.g4t2project.g4t2project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.g4t2project.g4t2project.repository.*;
import com.g4t2project.g4t2project.entity.*;
import com.g4t2project.g4t2project.entity.Package;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private PackageRepository packageRepository;

    @Autowired
    private CleaningTaskRepository cleaningTaskRepository;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private WorkerRepository workerRepository;

    // 1. placeOrder(packageID, propertyID, timeslot): CleaningTask
    public CleaningTask placeOrder(int clientId, int packageID, int propertyID, CleaningTask.Shift shift, LocalDate date) {
        // Fetch entities
        Client client = clientRepository.findById(clientId).orElseThrow(() -> new IllegalArgumentException("Client not found"));
        Property property = propertyRepository.findById(propertyID).orElseThrow(() -> new IllegalArgumentException("Property not found"));
        Package pkg = packageRepository.findById(packageID).orElseThrow(() -> new IllegalArgumentException("Package not found"));

        // Assign a worker (simplified logic for example)
        Worker worker = assignWorker(property, shift, date);

        CleaningTask cleaningTask = new CleaningTask(property, worker, shift, CleaningTask.Status.Scheduled, date, false);

        return cleaningTaskRepository.save(cleaningTask);
    }

    // Helper method to assign a worker (simplified)
    private Worker assignWorker(Property property, CleaningTask.Shift shift, LocalDate date) {
        // Implement your logic to find the best-matched worker
        Optional<Worker> optionalWorker = workerRepository.findFirstByAvailableTrue();
        return optionalWorker.orElseThrow(() -> new IllegalStateException("No available workers"));
    }

    // 2. rateSession(taskID, rating, comments): Feedback
    public Feedback rateSession(int clientId, int taskID, int rating, String comments) {
        CleaningTask task = cleaningTaskRepository.findById(taskID).orElseThrow(() -> new IllegalArgumentException("Cleaning task not found"));

        // Check if the task belongs to the client and is completed
        if (!task.getProperty().getClient().getClientId().equals(clientId)) {
            throw new IllegalArgumentException("This task does not belong to the client");
        }
        if (task.getStatus() != CleaningTask.Status.Completed) {
            throw new IllegalStateException("Cannot rate a task that is not completed");
        }

        Feedback feedback = new Feedback(rating, comments, task);
        feedbackRepository.save(feedback);

        // Associate feedback with the cleaning task
        task.setFeedback(feedback);
        cleaningTaskRepository.save(task);

        return feedback;
    }

    // 3. addProperty(clientID, address, postalCode, propertyType): Property
    public Property addProperty(int clientId, String address, String postalCode, String propertyType) {
        Client client = clientRepository.findById(clientId).orElseThrow(() -> new IllegalArgumentException("Client not found"));

        Property property = new Property(client, address, postalCode, propertyType);

        return propertyRepository.save(property);
    }

    // 4. modifyProperty(propertyID, newAddress, newPostalCode): boolean
    public boolean modifyProperty(int clientId, int propertyID, String newAddress, String newPostalCode) {
        Property property = propertyRepository.findById(propertyID).orElseThrow(() -> new IllegalArgumentException("Property not found"));

        // Check if the property belongs to the client
        if (!property.getClient().getClientId().equals(clientId)) {
            throw new IllegalArgumentException("This property does not belong to the client");
        }

        // Update property details
        property.setAddress(newAddress);
        property.setPostalCode(newPostalCode);

        // Save changes
        propertyRepository.save(property);
        return true;
    }

    // 5. submitFeedback(clientID, taskID, rating, comments): boolean
    public boolean submitFeedback(int clientId, int taskID, int rating, String comments) {
        try {
            rateSession(clientId, taskID, rating, comments);
            return true;
        } catch (Exception e) {
            // Handle exception as needed
            return false;
        }
    }

    // 6. selectPackage(clientID, packageID): Package
    public Package selectPackage(int clientId, int packageID) {
        Client client = clientRepository.findById(clientId).orElseThrow(() -> new IllegalArgumentException("Client not found"));
        Package selectedPackage = packageRepository.findById(packageID).orElseThrow(() -> new IllegalArgumentException("Package not found"));

        // Set as preferred package
        client.setPreferredPackage(selectedPackage);
        clientRepository.save(client);

        return selectedPackage;
    }
}
