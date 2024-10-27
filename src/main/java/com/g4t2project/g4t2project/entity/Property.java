package com.g4t2project.g4t2project.entity;
import jakarta.persistence.*;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
@Entity
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonBackReference
    private int propertyId;

    @ManyToOne
    @JoinColumn(name = "clientId")
    @JsonBackReference
    private Client client;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "packageId")
    private CleaningPackage pkg;

    @OneToMany(mappedBy = "property")
    @JsonBackReference
    private List<CleaningTask> cleaningTasks = new ArrayList<CleaningTask>();

    private int numberOfRooms;
    private String address;
    private double latitude;
    private double longitude;
    private String postalCode;
    private String propertyType;

    protected Property() {}

    public Property(Client client, CleaningPackage pkg, String address, double latitude, double longitude, String postalCode) {
        this.client = client;
        this.pkg = pkg;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.postalCode = postalCode;
    }

    public int getPropertyId() {
        return propertyId;
    }

    public Client getClient() {
        return client;
    }

    public String getAddress() {
        return address;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
    public List<CleaningTask> getCleaningTasks() {
        return cleaningTasks;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void addCleaningTask(CleaningTask cleaningTask) {
        cleaningTasks.add(cleaningTask);
    }

    public void removeCleaningTask(CleaningTask cleaningTask) {
        cleaningTasks.remove(cleaningTask);
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public CleaningPackage getPkg() {
        return pkg;
    }

    public void setPkg(CleaningPackage pkg) {
        this.pkg = pkg;
    }

    public int getNumberOfRooms() {
        return numberOfRooms;
    }

    public void setNumberOfRooms(int numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }
    
}

