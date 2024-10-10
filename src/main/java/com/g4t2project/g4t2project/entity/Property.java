package com.g4t2project.g4t2project.entity;
import jakarta.persistence.*;
import java.util.ArrayList;
@Entity
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int propertyId;

    @ManyToOne
    @JoinColumn(name = "clientId")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "packageId")
    private Package pkg;

    @OneToMany(mappedBy = "property")
    private ArrayList<CleaningTask> cleaningTasks = new ArrayList<CleaningTask>();

    private String address;
    private double latitude;
    private double longitude;

    protected Property() {}

    public Property(Client client, Package pkg, String address, double latitude, double longitude) {
        this.client = client;
        this.pkg = pkg;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
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
    public ArrayList<CleaningTask> getCleaningTasks() {
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

    

}
