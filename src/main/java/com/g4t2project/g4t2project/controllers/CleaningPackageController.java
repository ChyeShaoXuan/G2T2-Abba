package com.g4t2project.g4t2project.controllers;

import com.g4t2project.g4t2project.entity.*;
import com.g4t2project.g4t2project.service.*;
import com.g4t2project.g4t2project.DTO.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.g4t2project.g4t2project.entity.CleaningPackage.PackageType;
import com.g4t2project.g4t2project.entity.CleaningPackage.PropertyType;

@RestController
@RequestMapping("/packages")
@CrossOrigin(origins = "http://localhost:3000")

public class CleaningPackageController {

    @Autowired
    private CleaningPackageService cleaningPackageService;

    @GetMapping("/available")
    public ResponseEntity<List<CleaningPackage>> getAvailablePackages(
            @RequestParam PackageType packageType,
            @RequestParam PropertyType propertyType) {

        List<CleaningPackage> packages = cleaningPackageService.getAvailablePackages(packageType, propertyType);
        return ResponseEntity.ok(packages);
    }

    @PostMapping("/book")
    public ResponseEntity<String> bookPackage(@RequestBody CleaningPackage selectedPackage) {
        if (selectedPackage.isManualBookingRequired()) {
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                    .body("Please contact the sales team to complete the booking for this package.");
        } else {
            return ResponseEntity.ok("Booking confirmed for package: " + selectedPackage.getProperty_details());
        }
    }
}

