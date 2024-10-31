package com.g4t2project.g4t2project.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.g4t2project.g4t2project.DTO.*;
import com.g4t2project.g4t2project.repository.*;
import com.g4t2project.g4t2project.entity.*;
import com.g4t2project.g4t2project.entity.CleaningPackage.PackageType;
import com.g4t2project.g4t2project.entity.CleaningPackage.PropertyType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CleaningPackageService {

    public List<CleaningPackage> getAvailablePackages(PackageType packageType, PropertyType propertyType) {
        List<CleaningPackage> packages = new ArrayList<>();

        if (packageType == PackageType.BiWeekly) {
            packages.add(new CleaningPackage(packageType, propertyType, 138, 3.0, 23, "Promotion Bi-weekly - 3 Hours", 1, false));
            packages.add(new CleaningPackage(packageType, propertyType, 168, 4.0, 21, "Promotion Bi-weekly - 4 Hours", 1, false));
            packages.add(new CleaningPackage(packageType, propertyType, -1, 5.0, -1, "Promotion Bi-weekly - 5 Hours", -1, true)); // Manual booking
        }

        if (packageType == PackageType.Weekly) {
            if (propertyType == PropertyType.Hdb) {
                packages.add(new CleaningPackage(packageType, propertyType, -1, 2.0, -1, "2 Bedrooms and Below", -1, true)); 
                packages.add(new CleaningPackage(packageType, propertyType, 276, 3.0, 23, "3-Room", 1, false));
                packages.add(new CleaningPackage(packageType, propertyType, 336, 4.0, 21, "4-Room", 1, false));
                packages.add(new CleaningPackage(packageType, propertyType, -1, 5.0, -1, "5 Bedrooms or More", -1, true)); 
            } else if (propertyType == PropertyType.Condominium) {
                packages.add(new CleaningPackage(packageType, propertyType, 276, 3.0, 23, "2 Bedroom and Below", 1, false));
                packages.add(new CleaningPackage(packageType, propertyType, 336, 4.0, 21, "3 Bedroom", 1, false));
                packages.add(new CleaningPackage(packageType, propertyType, -1, 5.0, -1, "4 Bedrooms or More", -1, true)); 
                packages.add(new CleaningPackage(packageType, propertyType, -1, 2.5, 19, "Night Shift 2-3 Bedroom", 1, false));
            } else if (propertyType == PropertyType.Landed) {
                // No specific packages for landed properties, direct to manual booking
                packages.add(new CleaningPackage(packageType, propertyType, -1, -1.0, -1, "Weekly Cleaning for Landed Property", -1, true));
            }
        }
        return packages;
    }
}

