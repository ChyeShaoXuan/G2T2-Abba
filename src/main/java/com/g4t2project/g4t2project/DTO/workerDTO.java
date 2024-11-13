package com.g4t2project.g4t2project.DTO;

public class workerDTO {
    private Long workerId;
    private String password;

    public Long getWorkerId() { 
        return workerId;
    }
    public void setWorkerId(Long workerId) {
        this.workerId = workerId;
    }
    public String getPassword() {  
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    private String name;
    private String phoneNumber;
    private String shortBio;
    private boolean available;

    public workerDTO(Long workerId, String name, String phoneNumber, String shortBio, boolean available) {
        this.workerId = workerId;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.shortBio = shortBio;
        this.available = available;
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

}

