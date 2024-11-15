// File: src/main/java/com/g4t2project/g4t2project/entity/LeaveApplication.java
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

    private String reason;

    @Column(name = "mcDocumentSubmitted")
    private boolean mcDocumentSubmitted;

    @Lob
    @Column(name = "mcDocument", columnDefinition="LONGBLOB")
    private byte[] mcDocument;

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

    // Add getters and setters for mcDocument fields
    public byte[] getMcDocument() {
        return mcDocument;
    }

    public void setMcDocument(byte[] mcDocument) {
        this.mcDocument = mcDocument;
    }

    public String getMcDocumentUrl() {
        return mcDocumentUrl;
    }

    public void setMcDocumentUrl(String mcDocumentUrl) {
        this.mcDocumentUrl = mcDocumentUrl;
    }

    public boolean isMcDocumentSubmitted() {
        return mcDocumentSubmitted;
    }

    public void setMcDocumentSubmitted(boolean mcDocumentSubmitted) {
        this.mcDocumentSubmitted = mcDocumentSubmitted;
    }

    // Existing getters and setters
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

    public void setAdmin(Admin admin) {
        this.admin = admin;
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

    public void setSubmissionDateTime(LocalDateTime submissionDateTime) {
        this.submissionDateTime = submissionDateTime;
    }
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
    
}
