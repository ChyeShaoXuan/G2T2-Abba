package com.g4t2project.g4t2project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.g4t2project.g4t2project.entity.Package;

public interface PackageRepository extends JpaRepository<Package, Integer> {
}
