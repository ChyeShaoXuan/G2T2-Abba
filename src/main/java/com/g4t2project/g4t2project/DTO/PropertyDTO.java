package com.g4t2project.g4t2project.DTO;

import com.g4t2project.g4t2project.entity.CleaningPackage.PackageType;
import com.g4t2project.g4t2project.entity.CleaningPackage.PropertyType;
import com.g4t2project.g4t2project.entity.CleaningTask.Status;
import com.g4t2project.g4t2project.entity.CleaningTask.Shift;

public class PropertyDTO {
    private Long propertyId;
    private int numberOfRooms;
    private String address;
    private double latitude;
    private double longitude;
    private PackageType packageType; // Assuming PackageType is an enum
    private PropertyType propertyType; // Assuming PropertyType is an enum
    private double price; // Price from CleaningPackage
    private double hours; // Hours from CleaningPackage
    private int hourlyRate; // Hourly rate from CleaningPackage
    private int pax; // Pax from CleaningPackage
    private boolean manualBookingRequired; // Manual booking required field

    private Status taskStatus;
    private Shift taskShift;

    // Constructor
    public PropertyDTO(Long propertyId, int numberOfRooms, String address, 
                       PackageType packageType, PropertyType propertyType, double price, double hours, 
                       int hourlyRate, int pax, Status taskStatus, Shift taskShift) {
        this.propertyId = propertyId;
        this.numberOfRooms = numberOfRooms;
        this.address = address;
        // this.latitude = latitude;
        // this.longitude = longitude;
        this.packageType = packageType;
        this.propertyType = propertyType;
        this.price = price;
        this.hours = hours;
        this.hourlyRate = hourlyRate;
        this.pax = pax;
        this.taskStatus = taskStatus;
        this.taskShift = taskShift;
    }

    // Getters and Setters
    public Long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
    }

    public int getNumberOfRooms() {
        return numberOfRooms;
    }

    public void setNumberOfRooms(int numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public PackageType getPackageType() {
        return packageType;
    }

    public void setPackageType(PackageType packageType) {
        this.packageType = packageType;
    }

    public PropertyType getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(PropertyType propertyType) {
        this.propertyType = propertyType;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getHours() {
        return hours;
    }

    public void setHours(double hours) {
        this.hours = hours;
    }

    public int getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(int hourlyRate) {
        this.hourlyRate = hourlyRate;
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

    public Status getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(Status taskStatus) {
        this.taskStatus = taskStatus;
    }

    public Shift getTaskShift() {
        return taskShift;
    }

    public void setTaskShift(Shift taskShift) {
        this.taskShift = taskShift;
    }

}
