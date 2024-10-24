package com.g4t2project.g4t2project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.g4t2project.g4t2project.entity.CleaningTask;
import java.util.List;
import com.g4t2project.g4t2project.entity.Worker;
import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface CleaningTaskRepository extends JpaRepository<CleaningTask, Integer> {
    List<CleaningTask> findTasksByWorkerAndDate(Worker worker, LocalDate date);
    boolean existsByWorkerAndDateAndShift(Worker worker, LocalDate date, CleaningTask.Shift shift);

    // @Query("SELECT t FROM CleaningTask t WHERE t.worker.workerId = :workerId AND t.date = :date AND t.shift < :shift ORDER BY t.shift DESC")
    // Optional<CleaningTask> findLastTaskByWorkerBeforeShift(@Param("workerId") Long workerId, @Param("date") LocalDate date, @Param("shift") CleaningTask.Shift shift);
}
