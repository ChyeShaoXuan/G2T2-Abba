package com.g4t2project.g4t2project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.g4t2project.g4t2project.entity.LeaveApplication;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@Repository
public interface LeaveApplicationRepository extends JpaRepository<LeaveApplication, Integer> {

    // Custom query to find leave applications with missing MC documents
    // currently the leaveApplication entity does not have a field for mcDocumentSubmitted. Do we include this field in the entity?
    @Query("SELECT la FROM LeaveApplication la WHERE la.mcDocumentSubmitted = false")
    List<LeaveApplication> findPendingMcSubmissions();
    
    @Modifying
    @Transactional
    @Query("DELETE FROM LeaveApplication p WHERE p.worker.workerId = :workerId")
    void deletePropertyByWorkerId(Long workerId);
    
}
