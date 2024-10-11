package com.g4t2project.g4t2project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.g4t2project.g4t2project.entity.Worker;

@Repository
public interface WorkerRepository extends JpaRepository<Worker, Long> {
    //add custom query methods here if needed
}