package com.g4t2project.g4t2project.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.g4t2project.g4t2project.entity.*;
public interface WorkerRepository extends JpaRepository<Worker, Long> {
    Optional<Worker> findFirstByAvailableTrue();
}
