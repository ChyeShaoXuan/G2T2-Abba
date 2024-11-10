package com.g4t2project.g4t2project.entity;
import jakarta.persistence.*;
import java.util.*;

@Entity
public class CleaningPackage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long packageId;

    @Enumerated(EnumType.STRING)
    private PackageType packageType;

    @Enumerated(EnumType.STRING)
    private PropertyType propertyType;

    private int price;
    private double hours;
    private int hourly_rate;
    private String property_details;
    private int pax;
    private boolean manualBookingRequired;

    public enum PackageType {
        Weekly,
        BiWeekly
    }

    public enum PropertyType {
        Hdb,
        Condominium,
        Landed
    }
    

    @OneToMany(mappedBy = "pkg", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Property> properties = new ArrayList<Property>();

    

    protected CleaningPackage() {}

    // public CleaningPackage(PackageType packageType, int price, int hours, int hourly_rate, String property_details, ArrayList<Property> properties) {
    //     this.packageType = packageType;
    //     this.price = price;
    //     this.hours = hours;
    //     this.hourly_rate = hourly_rate;
    //     this.property_details = property_details;
    //     this.properties = properties;
    // }

    public CleaningPackage(PackageType packageType, PropertyType propertyType, int price, double hours, int hourlyRate, String propertyDetails, int pax, boolean manualBookingRequired) {
        this.packageType = packageType;
        this.propertyType = propertyType;
        this.price = price;
        this.hours = hours;
        this.hourly_rate = hourlyRate;
        this.property_details = propertyDetails;
        this.pax = pax;
        this.manualBookingRequired = manualBookingRequired;
    }

    public Long getPackageId() {
        return packageId;
    }

    public void setPackageId(Long packageId) {
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

    public double getHours() {
        return hours;
    }

    public void setHours(double hours) {
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

    public int getPax() {
        return pax;
    }

    public void setPax(int pax) {
        this.pax = pax;
    }

    public boolean isManualBookingRequired() {
        return manualBookingRequired;
    }

    public void setManualBookingRequired(boolean manualBookingRequired) {
        this.manualBookingRequired = manualBookingRequired;
    }
    
    public PropertyType getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(PropertyType propertyType) {
        this.propertyType = propertyType;
    }
}
