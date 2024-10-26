package com.g4t2project.g4t2project.DTO;

public class cleaningTaskDTO {
    private int propertyId;
    private String shift;
    private String date;
    private boolean Acknowledged = false;
    
    public cleaningTaskDTO(int propertyId, String shift, String date) {
        this.propertyId = propertyId;
        this.shift = shift;
        this.date = date;
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
        return Acknowledged;
    }

    public void setAcknowledged(boolean acknowledged) {
        Acknowledged = acknowledged;
    }


}
