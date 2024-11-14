package com.g4t2project.g4t2project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.g4t2project.g4t2project.DTO.OverwriteCleaningTaskDTO;
import com.g4t2project.g4t2project.entity.CleaningTask;
import java.util.List;
import com.g4t2project.g4t2project.entity.Worker;
import java.time.LocalDate;
import java.util.Optional;


@Repository
public interface CleaningTaskRepository extends JpaRepository<CleaningTask, Integer> {
    @Query("SELECT c FROM CleaningTask c WHERE c.worker = :worker AND c.date = :date")
    List<CleaningTask> findTasksByWorkerAndDate(@Param("worker") Worker worker, @Param("date") LocalDate date);

    @Query("SELECT c FROM CleaningTask c WHERE c.property.client.clientId = :clientId")
    List<CleaningTask> findTasksByClient(@Param("clientId") Integer clientId);

    @Query("SELECT t FROM CleaningTask t WHERE t.worker = :worker")
    Optional<CleaningTask> findTaskByWorker(@Param("worker") Worker worker);

    Optional<CleaningTask> findFirstByWorkerOrderByTaskIdDesc(Worker worker);
        
    @Query("SELECT t FROM CleaningTask t WHERE t.worker.workerId = :workerId")
    List<CleaningTask> findTasksByWorker(@Param("workerId") Integer workerId);

    @Modifying
    @Transactional
    @Query("DELETE FROM CleaningTask p WHERE p.worker.workerId = :workerId")
    void deleteTaskByWorkerId(Long workerId);

    @Query("SELECT ct FROM CleaningTask ct WHERE ct.property.propertyId = :propertyId AND ct.date = :date AND ct.shift = :shift")
    Optional<CleaningTask> findTaskByDateShiftProperty(Long propertyId, LocalDate date, CleaningTask.Shift shift);

    @Query("SELECT ct FROM CleaningTask ct WHERE ct.worker.workerId = :workerId AND ct.status IN :statuses")
    List<CleaningTask> findByWorkerIdAndStatusIn(@Param("workerId") Long workerId, @Param("statuses") List<CleaningTask.Status> statuses);

    @Query("SELECT t FROM CleaningTask t WHERE t.status = :assignedStatus")
    List<CleaningTask> findTasksWithoutArrivalConfirmation(@Param("assignedStatus") CleaningTask.Status assignedStatus);

    @Query("SELECT t FROM CleaningTask t WHERE t.status = 'Completed' AND t.worker.workerId = :workerId")
    List<CleaningTask> findCompletedTasksByWorker(@Param("workerId") Integer workerId);

    @Query("SELECT t FROM CleaningTask t WHERE t.status = 'Completed' AND t.property.client.clientId = :clientId")
    List<CleaningTask> findCompletedTasksByClient(@Param("clientId") Integer clientId);

}
