package com.g4t2project.g4t2project.DTO;

public class ClientDTO {
    private Long clientId;
    private String email;
    private String name;
    // private Long adminId; 
    // private Long packageId;
    // private Long workerId;
    private String phoneNumber;

    // Constructors
    public ClientDTO(Long clientId, String email, String name, String phoneNumber) {
        this.clientId = clientId;
        this.email = email;
        this.name = name;
        this.phoneNumber = phoneNumber;
        // this.adminId = adminId;
        // this.packageId = packageId;
        // this.workerId = workerId;
    }

    // Getters and Setters
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // public Long getAdminId() {
    //     return adminId;
    // }

    // public void setAdminId(Long adminId) {
    //     this.adminId = adminId;
    // }

    // public Long getPackageId() {
    //     return packageId;
    // }

    // public void setPackageId(Long packageId) {
    //     this.packageId = packageId;
    // }

    // public Long getWorkerId() {
    //     return workerId;
    // }

    // public void setWorkerId(Long workerId) {
    //     this.workerId = workerId;
    // }
}
