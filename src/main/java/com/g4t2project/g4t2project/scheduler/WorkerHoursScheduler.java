package com.g4t2project.g4t2project.scheduler;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.g4t2project.g4t2project.entity.Worker;
import com.g4t2project.g4t2project.entity.WorkerHours;
import com.g4t2project.g4t2project.repository.WorkerHoursRepository;
import com.g4t2project.g4t2project.repository.WorkerRepository;

@Component
public class WorkerHoursScheduler {

    @Autowired
    private WorkerHoursRepository workerHoursRepository;

    @Autowired
    private WorkerRepository workerRepository;


    // @Scheduled(cron = "0 14 21 * * *") // for testing!!!
    
    // Reset worker hours weekly and add weekly hours to worker_hours_stats table: totalHours
    @Scheduled(cron = "0 0 0 * * MON") // Runs at midnight every Monday
    public void resetWorkerHoursWeekly() {
        List<Worker> workers = workerRepository.findAll(); 

        for (Worker worker : workers) {
            // Fetch the corresponding WorkerHours record         WorkerHours entity in workerHoursRepository
            Optional<WorkerHours> optionalWorkerHours = workerHoursRepository.findById(worker.getWorkerId());

            if (optionalWorkerHours.isPresent()) {
                WorkerHours workerHours = optionalWorkerHours.get();

                // Add worker_hours_in_week to totalHoursWorked
                workerHours.setTotalHoursWorked(workerHours.getTotalHoursWorked() + worker.getWorkerHoursInWeek());

                workerHoursRepository.save(workerHours);
            }

            // Reset worker_hours_in_week for the new week
            worker.setWorkerHoursInWeek(0);
        }

        workerRepository.saveAll(workers);
    }
}

