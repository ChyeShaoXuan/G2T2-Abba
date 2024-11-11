package com.g4t2project.g4t2project.DTO;

import com.g4t2project.g4t2project.entity.*;
import java.time.LocalDate;

public class FindClosestWorkerDTO {
    private Long propertyId;
    private CleaningTask.Shift shift;
    private LocalDate date;

    public FindClosestWorkerDTO(Long propertyId, CleaningTask.Shift shift, LocalDate date) {
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
    public CleaningTask.Shift getShift() {
        return shift;
    }
    public void setShift(CleaningTask.Shift shift) {
        this.shift = shift;
    }
    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }    
    
}
