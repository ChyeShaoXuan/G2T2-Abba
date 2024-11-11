package com.g4t2project.g4t2project.controllers;

import com.g4t2project.g4t2project.entity.*;
import com.g4t2project.g4t2project.exception.NoAvailableWorkerException;
import com.g4t2project.g4t2project.service.CleaningTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import com.g4t2project.g4t2project.DTO.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import com.g4t2project.g4t2project.repository.*;
import com.g4t2project.g4t2project.service.*;

@RestController
@RequestMapping("/cleaningTasks")
@CrossOrigin(origins = "http://localhost:3000")
public class CleaningTaskController {

    @Autowired
    private CleaningTaskService cleaningTaskService;

    @Autowired
    private PropertyRepository propertyRepository;

    @PostMapping
    public ResponseEntity<String> createCleaningTask(@RequestBody cleaningTaskDTO taskDTO, @RequestParam Long clientId) {
        System.out.println("Creating cleaning Task");
        System.out.println("-------------------------");
        // Step 1: Validate the property ownership
        System.out.println(taskDTO.getPropertyId());
        System.out.println("---------------------------");
        Property property = cleaningTaskService.getPropertyById(taskDTO.getPropertyId());
        System.out.println("Finding property");
        System.out.println(property);
        System.out.println("---------------------------");

        if (property == null || !property.getClient().getClientId().equals(clientId)) {
            return new ResponseEntity<>("You are not authorized to create a task for this property", HttpStatus.FORBIDDEN);
        }

        System.out.println("Property found: " + property);
        // format cleanintaskDTO date
        // Define the expected date format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
        LocalDate parsed_date;
        try {
            // Parse the date from the DTO
            parsed_date = LocalDate.parse(taskDTO.getDate(), formatter);
        } catch (DateTimeParseException e) {
            // Handle the exception, e.g., log it and throw a custom exception or set a default value
            throw new IllegalArgumentException("Invalid date format: " + taskDTO.getDate(), e);
        } 

        // Step 2: Check for duplicate cleaning tasks with the same date, shift, and property
        boolean duplicateExists = cleaningTaskService.existsByDateAndShiftAndProperty(parsed_date, CleaningTask.Shift.valueOf(taskDTO.getShift()), property);
        if (duplicateExists) {
            return new ResponseEntity<>("A cleaning task with the same shift and date already exists for this property", HttpStatus.CONFLICT);
        }

         // Step 3: Convert DTO to entity
         CleaningTask cleaningTask = new CleaningTask();
         cleaningTask.setProperty(property);
         cleaningTask.setShift(CleaningTask.Shift.valueOf(taskDTO.getShift()));
         cleaningTask.setStatus(CleaningTask.Status.Pending);
         cleaningTask.setDate(parsed_date);
 
        // Step 4: add the cleaning task
        try {
            cleaningTaskService.addCleaningTask(cleaningTask); // Use the new method
        } catch (NoAvailableWorkerException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }catch (RuntimeException e) {
            return new ResponseEntity<>("An error occurred while creating the cleaning task: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("Cleaning task created successfully", HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CleaningTask>> getCleaningTasks(@RequestParam Integer clientId) {
        List<CleaningTask> tasks = cleaningTaskService.getCleaningTasksByClient(clientId);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @GetMapping("/tasks")
    public ResponseEntity<List<OverwriteCleaningTaskDTO>> getCleaningTasks() {
        List<OverwriteCleaningTaskDTO> tasks = cleaningTaskService.getAllCleaningTasks();
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<OverwriteCleaningTaskDTO> updateCleaningTask(@PathVariable Integer taskId, @RequestBody OverwriteCleaningTaskDTO taskDTO) {
        taskDTO.setTaskId(taskId); // Ensure the task ID is set correctly
        CleaningTask updatedTask = cleaningTaskService.updateCleaningTask(taskDTO);
        OverwriteCleaningTaskDTO updatedTaskDTO = cleaningTaskService.convertToDTO(updatedTask);
        return new ResponseEntity<>(updatedTaskDTO, HttpStatus.OK);
    }

    // @GetMapping("/closestWorker")
    // public Optional<Worker> findClosestWorker(
    //         @RequestBody Long propertyId,
    //         @RequestBody String shift,
    //         @RequestBody String date
    // ) {
    //     LocalDate taskDate = LocalDate.parse(date);
    //     CleaningTask.Shift taskShift = CleaningTask.Shift.valueOf(shift);

    //     Property taskProperty = propertyRepository.findById(propertyId).orElseThrow(() -> new IllegalArgumentException("Property not found"));

    //     Optional<Worker> closestWorker = cleaningTaskService.findClosestWorker(taskProperty, taskDate, taskShift);

    //     return closestWorker;
    // }

    @PostMapping("/closestWorker")
    public ResponseEntity<Worker> findClosestWorker(@RequestBody FindClosestWorkerDTO request) {
        Property taskProperty = propertyRepository.findById(request.getPropertyId())
        .orElseThrow(() -> new IllegalArgumentException("Property not found"));
    
        // Convert shift from String to CleaningTask.Shift enum
        CleaningTask.Shift shift;
        try {
            shift = CleaningTask.Shift.valueOf(request.getShift());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
        
        Optional<Worker> closestWorker = cleaningTaskService.findClosestWorker(taskProperty, request.getDate(), shift);
    
        return new ResponseEntity<>(closestWorker.orElse(null), HttpStatus.OK);
    }
}
 