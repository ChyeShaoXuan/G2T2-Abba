package com.g4t2project.g4t2project.DTO;

public class cleaningTaskDTO {
    private int propertyId;
    private String shift;
    private String date;
    private boolean acknowledged;
    
    public cleaningTaskDTO(int propertyId, String shift, String date, boolean acknowledged) {
        this.propertyId = propertyId;
        this.shift = shift;
        this.date = date;
        this.acknowledged = acknowledged;
    }   

    public int getPropertyId() {    
        return propertyId;
    }

    public void setPropertyId(int propertyId) {
        this.propertyId = propertyId;
    }

    public String getShift() {
        return shift;
    }

    public String getDate() {
        return date;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isAcknowledged() {
        return acknowledged;
    }

    public void setAcknowledged(boolean acknowledged) {
        this.acknowledged = acknowledged;
    }


}
