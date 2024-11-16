package com.g4t2project.g4t2project.DTO;

public class workerDetailsDTO {

    private Long workerId;
    private String emailID;
    private String name;
    private String phoneNumber;
    private String shortBio = "I am a worker";
    private boolean deployed;
    private long curPropertyId;
    private Integer worker_hours_in_week = 0;

    public workerDetailsDTO(Long workerId, String emailID, String name, String phoneNumber, String shortBio, boolean deployed, long curPropertyId, int worker_hours_in_week) {
        this.workerId = workerId;
        this.emailID = emailID;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.shortBio = shortBio;
        this.deployed = deployed;
        this.curPropertyId = curPropertyId;
        this.worker_hours_in_week = worker_hours_in_week;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public Long getWorkerId() { 
        return workerId;
    }
    public void setWorkerId(Long workerId) {
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

    public boolean isDeployed() {
        return deployed;
    }

    public void setDeployed(boolean deployed) {
        this.deployed = deployed;
    }

    public long getCurPropertyId() {
        return curPropertyId;
    }

    public void setCurPropertyId(long curPropertyId) {
        this.curPropertyId = curPropertyId;
    }

    public Integer getWorkerHoursInWeek() {
        return worker_hours_in_week;
    }

    public void setWorker_hours_in_week(int worker_hours_in_week) {
        this.worker_hours_in_week = worker_hours_in_week;
    }



}

