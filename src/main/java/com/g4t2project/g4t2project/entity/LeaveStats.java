package com.g4t2project.g4t2project.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "leave_stats")
public class LeaveStats {
    @Id
    private String monthYear; // Store in format "YYYY-MM"

    private int alCount; // Annual Leave count
    private int mcCount; // Medical Leave count
    private int hlCount; // Hospital Leave count
    private int elCount; // Emergency Leave count

    @ManyToOne
    @JoinColumn(name = "workerId")
    private Worker worker; // Adding the relationship with Worker

    // Default constructor
    protected LeaveStats() {}

    // Constructor
    public LeaveStats(String monthYear, int alCount, int mcCount, int hlCount, int elCount, Worker worker) {
        this.monthYear = monthYear;
        this.alCount = alCount;
        this.mcCount = mcCount;
        this.hlCount = hlCount;
        this.elCount = elCount;
        this.worker = worker; // Set the worker associated with the leave statistics
    }

    // Getters and Setters
    public String getMonthYear() {
        return monthYear;
    }

    public int getAlCount() {
        return alCount;
    }

    public void setAlCount(int alCount) {
        this.alCount = alCount;
    }

    public int getMcCount() {
        return mcCount;
    }

    public void setMcCount(int mcCount) {
        this.mcCount = mcCount;
    }

    public int getHlCount() {
        return hlCount;
    }

    public void setHlCount(int hlCount) {
        this.hlCount = hlCount;
    }

    public int getElCount() {
        return elCount;
    }

    public void setElCount(int elCount) {
        this.elCount = elCount;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }
}
