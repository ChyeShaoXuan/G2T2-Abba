package com.g4t2project.g4t2project.DTO;

import java.time.LocalDate;

public class LeaveApplicationDTO {
    private int leaveApplicationId;
    private String workerName;
    private String leaveType;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private Long adminId;  

    // Constructors, getters, and setters

    public LeaveApplicationDTO() {}

    public LeaveApplicationDTO(int leaveApplicationId, String workerName, String leaveType, LocalDate startDate, LocalDate endDate, String status) {
        this.leaveApplicationId = leaveApplicationId;
        this.workerName = workerName;
        this.leaveType = leaveType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    public int getLeaveApplicationId() {
        return leaveApplicationId;
    }

    public void setLeaveApplicationId(int leaveApplicationId) {
        this.leaveApplicationId = leaveApplicationId;
    }

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }
}
