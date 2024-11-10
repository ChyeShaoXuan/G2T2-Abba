package com.g4t2project.g4t2project.DTO;

import com.g4t2project.g4t2project.entity.CleaningTask;

public class PlaceOrderRequestDTO {
    private String packageType;
    // private Long propertyID;
    private String propertyType;
    private Integer numberOfRooms;
    private CleaningTask.Shift shift;
    private String date;
    private Long preferredWorkerId;

    public String getPackageType() {
        return packageType;
    }
    public void setPackageType(String packageType) {
        this.packageType = packageType;
    }
    // public Long getPropertyID() {
    //     return propertyID;
    // }
    // public void setPropertyID(Long propertyID) {
    //     this.propertyID = propertyID;
    // }
    public String getPropertyType() {
        return propertyType;
    }
    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }
    public Integer getNumberOfRooms() {
        return numberOfRooms;
    }
    public void setNumberOfRooms(Integer numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }
    public CleaningTask.Shift getShift() {
        return shift;
    }
    public void setShift(CleaningTask.Shift shift) {
        this.shift = shift;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public Long getPreferredWorkerId() {
        return preferredWorkerId;
    }
    public void setPreferredWorkerId(Long preferredWorkerId) {
        this.preferredWorkerId = preferredWorkerId;
    }    

}
