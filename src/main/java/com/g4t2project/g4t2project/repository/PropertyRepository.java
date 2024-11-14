package com.g4t2project.g4t2project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.g4t2project.g4t2project.entity.*;
import com.g4t2project.g4t2project.entity.CleaningPackage;
import com.g4t2project.g4t2project.entity.Client;
import com.g4t2project.g4t2project.entity.Property;

import java.util.Optional;

public interface PropertyRepository extends JpaRepository<Property, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM Property p WHERE p.client.clientId = :clientId")
    void deleteByClientId(Long clientId);
    // Optional<Property> findByClientAndNumberOfRooms(Client client, int numberOfRooms);
    // Optional<Property> findByClientAndCleaningPackage(Client client, CleaningPackage pkg);

    @Query("SELECT p FROM Property p WHERE p.client = :client AND p.pkg = :pkg")
    Optional<Property> findByClientAndCleaningPackage(@Param("client") Client client, @Param("pkg") CleaningPackage pkg);

    @Query("SELECT p FROM Property p WHERE p.propertyId IN :propertyIds")
    List<Property> findByPropertyIds(@Param("propertyIds") List<Long> propertyIds);
}
