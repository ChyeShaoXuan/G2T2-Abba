package com.g4t2project.g4t2project.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import com.g4t2project.g4t2project.entity.WorkerHours;
public interface WorkerHoursRepository extends JpaRepository<WorkerHours, Integer> {
    @Modifying
    @Transactional
    @Query("DELETE FROM WorkerHours p WHERE p.worker.workerId = :workerId")
    void deleteWorkerHourByWorkerId(Long workerId);

    @Query("SELECT w FROM WorkerHours w WHERE w.worker.workerId = :workerId")
    List<WorkerHours> findByWorker_WorkerId(Long workerId);
}
