package com.g4t2project.g4t2project.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.g4t2project.g4t2project.entity.CleaningPackage;
import com.g4t2project.g4t2project.entity.CleaningPackage.*;

public interface CleaningPackageRepository extends JpaRepository<CleaningPackage, Long> {
    // @Query("SELECT p FROM CleaningPackage p WHERE p.packageId = :packageId")
    // Optional<CleaningPackage> findByIdWithEagerFetching(@Param("packageId") Long packageId);
    Optional<CleaningPackage> findByPackageTypeAndPropertyType(PackageType packageType, PropertyType propertyType);
}
