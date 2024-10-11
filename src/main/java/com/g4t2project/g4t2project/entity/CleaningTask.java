package com.g4t2project.g4t2project.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class CleaningTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int taskId;

    @ManyToOne
    @JoinColumn(name = "propertyId")
    private Property property;

    @ManyToOne
    @JoinColumn(name = "workerId")
    private Worker worker;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "feedbackId")
    private Feedback feedback;

    @Enumerated(EnumType.STRING)
    private Shift shift;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDate date;
    private boolean Acknowledged = false;

    public enum Shift {
        Morning,
        Afternoon,
        Evening
    }

    public enum Status {
        Scheduled,
        InProgress,
        Cancelled,
        Completed,
    }

    protected CleaningTask() {}

    public CleaningTask(Property property, Worker worker, Shift shift, Status status, LocalDate date, boolean Acknowledged) {
        this.property = property;
        this.worker = worker;
        this.shift = shift;
        this.status = status;
        this.date = date;
        this.Acknowledged = Acknowledged;
    }

    public int getTaskId() {
        return taskId;
    }

    public Property getProperty() {
        return property;
    }

    public Worker getWorker() {
        return worker;
    }

    public CleaningTask.Shift getShift() {
        return shift;
    }

    public CleaningTask.Status getStatus() {
        return status;
    }

    public LocalDate getDate() {
        return date;
    }

    public boolean isAcknowledged() {
        return Acknowledged;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public void setShift(CleaningTask.Shift shift) {
        this.shift = shift;
    }

    public void setStatus(CleaningTask.Status status) {
        this.status = status;
    }

    public void setAcknowledged(boolean acknowledged) {
        Acknowledged = acknowledged;
    }
    
    public void setFeedback(Feedback feedback) {
        this.feedback = feedback;
    }    
    
}
