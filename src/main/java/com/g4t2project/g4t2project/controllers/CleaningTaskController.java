package com.g4t2project.g4t2project.controllers;

import com.g4t2project.g4t2project.entity.CleaningTask;
import com.g4t2project.g4t2project.service.CleaningTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cleaningTasks")
public class CleaningTaskController {

    @Autowired
    private CleaningTaskService cleaningTaskService;

    @PostMapping
    public ResponseEntity<?> createCleaningTask(@RequestBody CleaningTask cleaningTask) {
        cleaningTaskService.addCleaningTask(cleaningTask);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
 