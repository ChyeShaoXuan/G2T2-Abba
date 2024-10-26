package com.g4t2project.g4t2project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.g4t2project.g4t2project.entity.CleaningTask;
import java.util.List;
import com.g4t2project.g4t2project.entity.Worker;
import java.time.LocalDate;

@Repository
public interface CleaningTaskRepository extends JpaRepository<CleaningTask, Integer> {
    @Query("SELECT c FROM CleaningTask c WHERE c.worker = :worker AND c.date = :date")
    List<CleaningTask> findTasksByWorkerAndDate(@Param("worker") Worker worker, @Param("date") LocalDate date);

}
