package com.g4t2project.g4t2project.controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.g4t2project.g4t2project.DTO.OverwriteCleaningTaskDTO;
import com.g4t2project.g4t2project.DTO.PropertyDTO;
import com.g4t2project.g4t2project.DTO.cleaningTaskDTO;
import com.g4t2project.g4t2project.entity.CleaningPackage;
import com.g4t2project.g4t2project.entity.CleaningTask;
import com.g4t2project.g4t2project.entity.Property;
import com.g4t2project.g4t2project.entity.Worker;
import com.g4t2project.g4t2project.exception.NoAvailableWorkerException;
import com.g4t2project.g4t2project.repository.CleaningTaskRepository;
import com.g4t2project.g4t2project.repository.PropertyRepository;
import com.g4t2project.g4t2project.repository.WorkerRepository;

import com.g4t2project.g4t2project.service.CleaningTaskService;

@RestController
@RequestMapping("/cleaningTasks")
@CrossOrigin(origins = "http://localhost:3000")
public class CleaningTaskController {

    @Autowired
    private CleaningTaskService cleaningTaskService;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private WorkerRepository workerRepository;

    @Autowired
    private CleaningTaskRepository cleaningTaskRepository;

    @GetMapping("/tasks/myJobs/{workerId}")
    public ResponseEntity<?> getJobDisplay(@PathVariable Integer workerId) {
        System.out.println("endpoint hit");
        Long workerIdLong = workerId.longValue();
        // find worker pass the check
        Worker worker = workerRepository.findById(workerIdLong).orElse(null);
        if (worker == null) {
            return new ResponseEntity<>("Worker not found", HttpStatus.NOT_FOUND);
        }

        // Step 2: Fetch cleaning tasks for the worker with status "Pending" or "Scheduled"
        List<CleaningTask> tasks = cleaningTaskRepository.findByWorkerIdAndStatusIn(workerIdLong, Arrays.asList(CleaningTask.Status.Pending, CleaningTask.Status.Scheduled));
        if (tasks.isEmpty()) {
            return new ResponseEntity<>("No assigned cleaning tasks found for this worker", HttpStatus.NOT_FOUND);
        }

        // Step 3: Extract all unique propertyIds from the tasks
        List<Long> propertyIds = tasks.stream()
                .map(task -> task.getProperty().getPropertyId())
                .distinct()
                .collect(Collectors.toList());

        // return new ResponseEntity<>(propertyIds, HttpStatus.OK);
        // Step 4: Fetch properties that match the propertyIds (use a single query to avoid repeated queries)
        List<Property> properties = propertyRepository.findByPropertyIds(propertyIds);

        // return new ResponseEntity<>(properties, HttpStatus.OK);
        // Step 5: Map tasks to cleaning packages and return them
        List<PropertyDTO> propertyDTOs = properties.stream().map(property -> {
            // For each property, we will get the associated cleaning package and map them to DTOs
            CleaningPackage cleaningPackage = property.getPkg(); // Retrieve the cleaning package associated with the property

            List<CleaningTask> relatedTasks = tasks.stream()
                    .filter(task -> task.getProperty().getPropertyId().equals(property.getPropertyId()))
                    .collect(Collectors.toList());

            CleaningTask cleaningTask = relatedTasks.isEmpty() ? null : relatedTasks.get(0);

            // Ensure a cleaning package exists
            if (cleaningPackage != null) {
                return new PropertyDTO(
                        property.getPropertyId(),
                        property.getNumberOfRooms(),
                        property.getAddress(),
                        cleaningPackage.getPackageType(),
                        cleaningPackage.getPropertyType(),
                        cleaningPackage.getPrice(),
                        cleaningPackage.getHours(),
                        cleaningPackage.getHourly_rate(),
                        cleaningPackage.getPax(),
                        cleaningTask.getStatus(),
                        cleaningTask.getShift()
                );
            }
            return null;
        }).filter(dto -> dto != null).collect(Collectors.toList());

        return new ResponseEntity<>(propertyDTOs, HttpStatus.OK);

    }

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
        // format cleaningtaskDTO date
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
            // startReminderService(cleaningTask); // Start sending reminders to the assigned worker
        } catch (NoAvailableWorkerException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("An error occurred while creating the cleaning task: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("Cleaning task created successfully", HttpStatus.CREATED);
    }

    // Function to get all cleaning tasks for a worker
    @GetMapping("/tasks/{workerId}")
    public ResponseEntity<List<OverwriteCleaningTaskDTO>> getWorkerCleaningTask(@PathVariable Integer workerId) {
        List<OverwriteCleaningTaskDTO> tasks = cleaningTaskService.getCleaningTasksById(workerId);
        System.out.println("Workers cleaning tasks: ");
        System.out.println(tasks);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    // @GetMapping
    // public ResponseEntity<List<CleaningTask>> getCleaningTasks(@RequestParam Integer clientId) {
    //     List<CleaningTask> tasks = cleaningTaskService.getCleaningTasksByClient(clientId);
    //     return new ResponseEntity<>(tasks, HttpStatus.OK);
    // }

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

    @PostMapping("/{taskId}/confirmArrival")
    public ResponseEntity<String> confirmArrival(@PathVariable Integer taskId, @RequestParam("photo") MultipartFile photo) {
        System.out.println("Confirming arrival for task " + taskId);
        try {
            cleaningTaskService.confirmArrival(taskId, photo);
            return ResponseEntity.ok("Arrival confirmed successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error confirming arrival: " + e.getMessage());
        }
    }

    @PostMapping("/{taskId}/confirmCompletion")
    public ResponseEntity<String> confirmCompletion(@PathVariable Integer taskId, @RequestParam("photo") MultipartFile photo) {
        try {
            cleaningTaskService.confirmCompletion(taskId, photo);
            return ResponseEntity.ok("Completion confirmed successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error confirming completion: " + e.getMessage());
        }
    }

    @GetMapping("/{taskId}/arrivalPhoto")
    public ResponseEntity<byte[]> getArrivalPhoto(@PathVariable Integer taskId) {
        CleaningTask task = cleaningTaskService.getCleaningTaskById(taskId);
        byte[] photo = task.getArrivalPhoto();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"arrival_photo.jpg\"")
                .contentType(MediaType.IMAGE_JPEG)
                .body(photo);
    }
}
