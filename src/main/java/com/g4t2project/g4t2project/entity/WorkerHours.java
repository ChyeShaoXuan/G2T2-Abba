package com.g4t2project.g4t2project.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "worker_hours_stats", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"worker_id", "monthYear"})
})
public class WorkerHours {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int workerHoursId;

    @ManyToOne
    @JoinColumn(name = "worker_id", referencedColumnName = "worker_id")
    private Worker worker;

    private String monthYear; // Store in format "YYYY-MM"

    private int totalHoursWorked;
    private int overtimeHours;

    // Default constructor
    protected WorkerHours() {}

    // Constructor
    public WorkerHours(Worker worker, String monthYear, int totalHoursWorked, int overtimeHours) {
        this.worker = worker;
        this.monthYear = monthYear;
        this.totalHoursWorked = totalHoursWorked;
        this.overtimeHours = overtimeHours;
    }

    // Getters and Setters
    public int getWorkerHoursId() {
        return workerHoursId;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public String getMonthYear() {
        return monthYear;
    }


    public int getTotalHoursWorked() {
        return totalHoursWorked;
    }

    public void setTotalHoursWorked(int totalHoursWorked) {
        this.totalHoursWorked = totalHoursWorked;
    }

    public int getOvertimeHours() {
        return overtimeHours;
    }

    public void setOvertimeHours(int overtimeHours) {
        this.overtimeHours = overtimeHours;
    }
}
