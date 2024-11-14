package com.g4t2project.g4t2project.entity;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
@Entity
public class Worker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
    @Column(name = "WorkerId")
    private Integer workerId;

    @ManyToOne
    @JoinColumn(name = "adminId")
    @JsonBackReference(value = "adminId")
    private Admin admin;
    
    @OneToMany(mappedBy = "preferredWorker", targetEntity = Client.class, cascade = CascadeType.ALL, orphanRemoval = true) // Establishing One-to-Many relationship
    private List<Client> clients;

    @OneToMany(mappedBy = "worker", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CleaningTask> cleaningTasks;

    @OneToMany(mappedBy = "worker", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkerHours> workerHours;

    @OneToMany(mappedBy = "worker", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LeaveStats> leaveStats;

    
    private String name;
    private String phoneNumber;
    private String shortBio;
    private boolean deployed;
    private String emailID;
    private long curPropertyId = 100;
    private boolean available;

    @Column(name = "worker_hours_in_week")
    private Integer worker_hours_in_week;

    @OneToOne
    private User user;

    public Worker() {}

    public Worker(Admin admin, String name, String phoneNumber, String shortBio, boolean deployed, String emailID, long curPropertyId, int worker_hours_in_week) {
        this.admin = admin;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.shortBio = shortBio;
        this.deployed = deployed;
        this.emailID = emailID;
        this.curPropertyId = curPropertyId;
        this.available = true;
        this.worker_hours_in_week = worker_hours_in_week;
    }

    public Integer getWorkerId() {
        return workerId;
    }


    public Long getAdminId() {
        return admin.getAdminId();
    }

    public String getName() {
        return name;
    }
    
    
    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getShortBio() {
        return shortBio;
    }

    public boolean getDeployed() {
        return deployed;
    }

    public String getTele_Id() {
        return emailID;
    }

    public long getCurPropertyId() {
        return curPropertyId;
    }

    public void setCurPropertyId(long curPropertyId) {
        this.curPropertyId = curPropertyId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setShortBio(String shortBio) {
        this.shortBio = shortBio;
    }

    public void setDeployed(boolean deployed) {
        this.deployed = deployed;
    }

    public void setemailID(String emailID) {
        this.emailID = emailID;
    }
    // probably no need for this method
    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean isAvailableOn(LocalDate date, CleaningTask.Shift shift) {
        // Check if the worker has any conflicting tasks on the given date and shift
        for (CleaningTask task : cleaningTasks) {
            if (task.getDate().equals(date) && task.getShift() == shift) {
                return false; // Worker already has a task for the given date and shift
            }
        }
        // If no conflicting tasks or leaves, the worker is available
        return true;
    }

    public Integer getWorkerHoursInWeek() {
        return worker_hours_in_week;
    }

    public void setWorkerHoursInWeek(int worker_hours_in_week) {
        this.worker_hours_in_week = worker_hours_in_week;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
