package com.g4t2project.g4t2project.entity;
import jakarta.persistence.*;
import java.util.*;

@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clientId;

    private String name;
    private String phoneNumber;
    private String email;

    @ManyToOne // Establishing Many-to-One relationship
    @JoinColumn(name = "workerId") // Foreign key in Client table
    private Worker preferredWorker;
    
    @OneToMany(mappedBy = "client",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Property> properties = new ArrayList<Property>();


    @ManyToOne
    @JoinColumn(name = "packageId")
    private CleaningPackage preferredPackage;

    @ManyToOne
    @JoinColumn(name = "adminId")
    private Admin admin;

    protected Client() {}

    public Client(Admin admin, CleaningPackage preferredPackage, Worker preferredWorker, String name, String phoneNumber, String email) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.admin = admin;
        this.preferredPackage = preferredPackage;
        this.preferredWorker = preferredWorker;
    }
    public void setAdmin(Admin admin) {
        this.admin = admin;
    }
    
    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public Worker getPreferredWorker() {
        return preferredWorker;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPreferredWorker(Worker preferredWorker) {
        this.preferredWorker = preferredWorker;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public CleaningPackage getPreferredPackage() {
        return preferredPackage;
    }

    public void setPreferredPackage(CleaningPackage preferredPackage) {
        this.preferredPackage = preferredPackage;
    }

    public Admin getAdmin() {
        return admin;
    }
    
}
