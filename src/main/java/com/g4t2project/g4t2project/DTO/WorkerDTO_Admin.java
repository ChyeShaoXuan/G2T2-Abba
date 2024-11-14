package com.g4t2project.g4t2project.DTO;

public class WorkerDTO_Admin {
    private Integer workerId;
    private String name;
    private String phoneNumber;
    private String shortBio;
    private boolean available;
    private boolean deployed;
    private Long curPropertyId;
    private String emailID;
    private Long adminId;

    // Constructors, getters, and setters

    public WorkerDTO_Admin() {}

    public WorkerDTO_Admin(Integer workerId, String name, String phoneNumber, String shortBio, boolean available, boolean deployed, Long curPropertyId, String emailID, Long adminId) {
        this.workerId = workerId;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.shortBio = shortBio;
        this.available = available;
        this.deployed = deployed;
        this.curPropertyId = curPropertyId;
        this.emailID = emailID;
        this.adminId = adminId;
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public Integer getWorkerId() {
        return workerId;
    }

    public void setWorkerId(Integer workerId) {
        this.workerId = workerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getShortBio() {
        return shortBio;
    }

    public void setShortBio(String shortBio) {
        this.shortBio = shortBio;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean isDeployed() {
        return deployed;
    }

    public void setDeployed(boolean deployed) {
        this.deployed = deployed;
    }

    public Long getCurPropertyId() {
        return curPropertyId;
    }

    public void setCurPropertyId(Long curPropertyId) {
        this.curPropertyId = curPropertyId;
    }
}