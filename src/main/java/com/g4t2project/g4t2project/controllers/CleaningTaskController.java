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
import com.g4t2project.g4t2project.DTO.OverwriteCleaningTaskDTO;
import com.g4t2project.g4t2project.DTO.cleaningTaskDTO;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


@RestController
@RequestMapping("/cleaningTasks")
@CrossOrigin(origins = "http://localhost:3000")
public class CleaningTaskController {

    @Autowired
    private CleaningTaskService cleaningTaskService;

    @PostMapping
    public ResponseEntity<String> createCleaningTask(@RequestBody cleaningTaskDTO taskDTO, @RequestParam Long clientId) {
        System.out.println("Creating cleaning Task");
        System.out.println("-------------------------");
        // Step 1: Validate the property ownership
        Property property = cleaningTaskService.getPropertyById((long)taskDTO.getPropertyId());
        if (property == null || !property.getClient().getClientId().equals(clientId)) {
            return new ResponseEntity<>("You are not authorized to create a task for this property", HttpStatus.FORBIDDEN);
        }
        System.out.println("Property found: " + property);

        // Step 2: Convert DTO to entity
        CleaningTask cleaningTask = new CleaningTask();
        cleaningTask.setProperty(property);
        cleaningTask.setShift(CleaningTask.Shift.valueOf(taskDTO.getShift()));
        // Define the expected date format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");

        try {
            // Parse the date from the DTO
            cleaningTask.setDate(LocalDate.parse(taskDTO.getDate(), formatter));
        } catch (DateTimeParseException e) {
            // Handle the exception, e.g., log it and throw a custom exception or set a default value
            throw new IllegalArgumentException("Invalid date format: " + taskDTO.getDate(), e);
        }
        cleaningTask.setStatus(CleaningTask.Status.Pending);

        // Step 3: add the cleaning task
        try {
            cleaningTaskService.addCleaningTask(cleaningTask); // Use the new method
        } catch (NoAvailableWorkerException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (RuntimeException e) {
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
}
 