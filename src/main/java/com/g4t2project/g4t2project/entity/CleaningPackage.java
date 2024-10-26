package com.g4t2project.g4t2project.entity;
import jakarta.persistence.*;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class CleaningPackage {
    @Id
    @JsonBackReference
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int packageId;

    @Enumerated(EnumType.STRING)
    private PackageType packageType;

    private int price;
    private int hours;
    private int hourly_rate;
    private String property_details;

    public enum PackageType {
        Weekly,
        BiWeekly
    }

    @OneToMany(mappedBy = "pkg")
    private List<Property> properties = new ArrayList<Property>();

    

    protected CleaningPackage() {}

    public CleaningPackage(PackageType packageType, int price, int hours, int hourly_rate, String property_details, ArrayList<Property> properties) {
        this.packageType = packageType;
        this.price = price;
        this.hours = hours;
        this.hourly_rate = hourly_rate;
        this.property_details = property_details;
        this.properties = properties;
    }

    public int getPackageId() {
        return packageId;
    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }

    public PackageType getPackageType() {
        return packageType;
    }

    public void setPackageType(PackageType packageType) {
        this.packageType = packageType;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getHourly_rate() {
        return hourly_rate;
    }

    public void setHourly_rate(int hourly_rate) {
        this.hourly_rate = hourly_rate;
    }

    public String getProperty_details() {
        return property_details;
    }

    public void setProperty_details(String property_details) {
        this.property_details = property_details;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(ArrayList<Property> properties) {
        this.properties = properties;
    }

    
}
