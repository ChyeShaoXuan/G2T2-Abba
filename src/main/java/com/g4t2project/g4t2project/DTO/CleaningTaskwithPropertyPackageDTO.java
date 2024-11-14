package com.g4t2project.g4t2project.DTO;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import com.g4t2project.g4t2project.entity.CleaningTask;
import com.g4t2project.g4t2project.entity.CleaningTask.Status;

public class CleaningTaskwithPropertyPackageDTO {
    private Long taskId;
    private CleaningTask.Status status;
    private LocalDate date;
    private Long propertyId;
    private int numberOfRooms;
    private String address;
    private Double latitude;
    private Double longitude;
    private String packageType;
    private Double price;
    // Constructor
    

    // Getters and Setters

    public Long getTaskId() {
        return taskId;
    }

    public CleaningTaskwithPropertyPackageDTO(Long taskId, Status status, Date date, Long propertyId,
                                              int numberOfRooms, String address, Double latitude, Double longitude,
                                              String packageType, Double price) {
        this.taskId = taskId;
        this.status = status;
        this.date = (date != null) ? date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null; // Convert to LocalDate if needed
        this.propertyId = propertyId;
        this.numberOfRooms = numberOfRooms;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.packageType = packageType;
        this.price = price;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public CleaningTask.Status getStatus() {
        return status;
    }

    public void setStatus(CleaningTask.Status status) {
        this.status = status;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

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

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getPackageType() {
        return packageType;
    }

    public void setPackageType(String packageType) {
        this.packageType = packageType;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
