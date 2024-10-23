package com.g4t2project.g4t2project.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.g4t2project.g4t2project.entity.*;
public interface WorkerRepository extends JpaRepository<Worker, Long> {
    Optional<Worker> findFirstByAvailableTrue();
    
     // Custom JPQL query to find an available worker for a specific date and shift
     @Query("SELECT w FROM Worker w WHERE w.available = true AND w.deployed = false AND NOT EXISTS (" +
     "SELECT t FROM CleaningTask t WHERE t.worker = w AND t.date = :date AND t.shift = :shift)")
    Optional<Worker> findAvailableWorker(@Param("date") LocalDate date, @Param("shift") Shift shift);
}
