package com.g4t2project.g4t2project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.g4t2project.g4t2project.entity.LeaveApplication;

@Repository
public interface LeaveApplicationRepository extends JpaRepository<LeaveApplication, Integer> {
    
}
