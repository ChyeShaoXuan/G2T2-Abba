package com.g4t2project.g4t2project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


import com.g4t2project.g4t2project.entity.*;


public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByName(String name);
    
}
