package com.g4t2project.g4t2project;

import java.util.Date;

public class LeaveApplication {
    private String leaveID;
    private Worker worker;
    private String leaveType; // MC, AL, EL
    private Date startDate;
    private Date endDate;
    private String status;

    // Constructor
    public LeaveApplication(String leaveID, Worker worker, String leaveType, Date startDate, Date endDate, String status) {
        this.leaveID = leaveID;
        this.worker = worker;
        this.leaveType = leaveType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    // Method to apply for leave
    public boolean applyLeave(String Employee_ID, Date startDate, Date endDate, String leaveType) {
        // Check if the workerID matches the worker's ID
        if (this.worker.getEmployee_ID().equals(Employee_ID)) {
            this.startDate = startDate;
            this.endDate = endDate;
            this.leaveType = leaveType;
            this.status = "Applied"; // Set status to applied
            return true; // Leave application successful
        }
        return false; // Leave application failed
    }

    // Getters and setters (optional)
    public String getLeaveID() {
        return leaveID;
    }

    public Worker getWorker() {
        return worker;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Other methods (if necessary) can be added here
}

