package com.g4t2project.g4t2project.entity;
import jakarta.persistence.*;
import java.util.ArrayList;


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
    
    @OneToMany(mappedBy = "client")
    private ArrayList<Property> properties = new ArrayList<Property>();

    @ManyToOne
    @JoinColumn(name = "packageId")
    private CleaningPackage preferredPackage;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;

    protected Client() {}

    public Client(String name, String phoneNumber, String email) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }
    public void setAdmin(Admin admin) {
        this.admin = admin;
    }
    
    

    public ArrayList<Property> getProperties() {
        return properties;
    }

    public void setProperties(ArrayList<Property> properties) {
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
