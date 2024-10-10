package com.g4t2project.g4t2project.entity;

import jakarta.persistence.*;

@Entity
public class JobStats {
    @Id
    private String monthYear; 

    private int totalHours;
    private int totalCleaningTasks;
    private int totalClients;
    private int totalProperties;
    private int totalWorkers;
    private int totalPackages;

    // Default constructor
    protected JobStats() {}

    // Constructor
    public JobStats(String monthYear, int totalHours, int totalCleaningTasks, int totalClients, int totalProperties, int totalWorkers, int totalPackages) {
        this.monthYear = monthYear;
        this.totalHours = totalHours;
        this.totalCleaningTasks = totalCleaningTasks;
        this.totalClients = totalClients;
        this.totalProperties = totalProperties;
        this.totalWorkers = totalWorkers;
        this.totalPackages = totalPackages;
    }

    // Getters and Setters
    public String getMonthYear() {
        return monthYear;
    }

    public int getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(int totalHours) {
        this.totalHours = totalHours;
    }

    public int getTotalCleaningTasks() {
        return totalCleaningTasks;
    }

    public void setTotalCleaningTasks(int totalCleaningTasks) {
        this.totalCleaningTasks = totalCleaningTasks;
    }

    public int getTotalClients() {
        return totalClients;
    }

    public void setTotalClients(int totalClients) {
        this.totalClients = totalClients;
    }

    public int getTotalProperties() {
        return totalProperties;
    }

    public void setTotalProperties(int totalProperties) {
        this.totalProperties = totalProperties;
    }

    public int getTotalWorkers() {
        return totalWorkers;
    }

    public void setTotalWorkers(int totalWorkers) {
        this.totalWorkers = totalWorkers;
    }

    public int getTotalPackages() {
        return totalPackages;
    }

    public void setTotalPackages(int totalPackages) {
        this.totalPackages = totalPackages;
    }
}
