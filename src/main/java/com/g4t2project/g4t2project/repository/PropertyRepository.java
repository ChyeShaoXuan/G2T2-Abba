package com.g4t2project.g4t2project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.g4t2project.g4t2project.entity.*;

public interface PropertyRepository extends JpaRepository<Property, Long> {

  @Modifying
  @Transactional
@Query("DELETE FROM Property p WHERE p.client.clientId = :clientId")
    void deleteByClientId(Long clientId);
}
