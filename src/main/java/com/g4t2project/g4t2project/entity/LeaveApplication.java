package com.g4t2project.g4t2project.entity;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class LeaveApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int leaveApplicationId;

    @ManyToOne
    @JoinColumn(name = "workerId")
    private Worker worker;

    @ManyToOne
    @JoinColumn(name = "adminId")
    private Admin admin;

    private LocalDate startDate;
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private LeaveType leaveType;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime submissionDateTime;

    private String mcDocumentUrl;

    boolean mcDocumentSubmitted;

    public enum LeaveType {
        MC,
        AL,
        HL,
        EL
    }

    public enum Status {
        Pending,
        Approved,
        Rejected
    }

    protected LeaveApplication() {}

    public LeaveApplication(Worker worker, Admin admin, LocalDate startDate, LocalDate endDate, LeaveType leaveType, LocalDateTime submissionDateTime) {
        this.worker = worker;
        this.admin = admin;
        this.startDate = startDate;
        this.endDate = endDate;
        this.leaveType = leaveType;
        this.status = Status.Pending;
        this.submissionDateTime = submissionDateTime;
    }

    public int getLeaveApplicationId() {
        return leaveApplicationId;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public Admin getAdmin() {
        return admin;
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

    public LeaveType getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(LeaveType leaveType) {
        this.leaveType = leaveType;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getSubmissionDateTime() {
        return submissionDateTime;
    }

    public void setMcDocumentUrl(String mcDocumentUrl) {
        this.mcDocumentUrl = mcDocumentUrl;
    }
    
}
