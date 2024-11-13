package com.g4t2project.g4t2project.DTO;

public class cleaningTaskDTO {
    private Long propertyId;
    private String shift;
    private String date;
    private boolean acknowledged;
    private workerDTO worker;
    
    public cleaningTaskDTO(Long propertyId, String shift, String date, boolean acknowledged, workerDTO worker) {
        this.propertyId = propertyId;
        this.shift = shift;
        this.date = date;
        this.acknowledged = acknowledged;
        this.worker = worker;
    }   

    public Long getPropertyId() {    
        return propertyId;
    }

    public void setPropertyId(Long propertyId) {
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

    public workerDTO getWorker() {
        return worker;
    }

    public void setWorker(workerDTO worker) {
        this.worker = worker;
    }
}
