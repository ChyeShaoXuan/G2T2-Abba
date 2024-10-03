package com.g4t2project.g4t2project.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "worker")
public class Worker extends Employee {

    @Column(name = "working_hours")
    private String workingHours;

    @Column(name = "phone_no")
    private int phoneNo;

    @Column(name = "direct_supervisor")
    private String directSupervisor;  // This will refer to the Admin's name

    @Column(name = "short_bio")
    private String shortBio;

    @Column(name = "deployed")
    private Boolean deployed;

    // Getters and setters
    public String getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(String workingHours) {
        this.workingHours = workingHours;
    }

    public int getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(int phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getDirectSupervisor() {
        return directSupervisor;
    }

    public void setDirectSupervisor(String directSupervisor) {
        this.directSupervisor = directSupervisor;
    }

    public String getShortBio() {
        return shortBio;
    }

    public void setShortBio(String shortBio) {
        this.shortBio = shortBio;
    }

    public Boolean getDeployed() {
        return deployed;
    }

    public void setDeployed(Boolean deployed) {
        this.deployed = deployed;
    }
}
