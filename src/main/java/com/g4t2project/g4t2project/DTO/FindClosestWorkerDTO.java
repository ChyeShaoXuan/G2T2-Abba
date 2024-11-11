package com.g4t2project.g4t2project.DTO;

import com.g4t2project.g4t2project.entity.*;
import java.time.LocalDate;

public class FindClosestWorkerDTO {
    private Long propertyId;
    private String shift;
    private LocalDate date;

    public FindClosestWorkerDTO(Long propertyId, String shift, LocalDate date) {
        this.propertyId = propertyId;
        this.shift = shift;
        this.date = date;
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
    public void setShift(String shift) {
        this.shift = shift;
    }
    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }    
    
}
