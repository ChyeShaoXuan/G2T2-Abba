package com.g4t2project.g4t2project.entity;
import jakarta.persistence.*;
import java.util.List;
@Entity
public class Worker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int workerId;

    @ManyToOne
    @JoinColumn(name = "adminId")
    private Admin admin;

    @OneToMany(mappedBy = "preferredWorker", targetEntity = Client.class) // Establishing One-to-Many relationship
    private List<Client> clients;

    @OneToMany(mappedBy = "worker")
    private List<CleaningTask> cleaningTasks;

    @OneToMany(mappedBy = "worker")
    private List<WorkerHours> workerHours;

    @OneToMany(mappedBy = "worker")
    private List<LeaveStats> leaveStats;

    private String name;
    private String phoneNumber;
    private String shortBio;
    private boolean deployed;
    private String tele_Id;
    private int curPropertyId = 0;

    protected Worker() {}

    public Worker(Admin admin, String name, String phoneNumber, String shortBio, boolean deployed, String tele_Id, int curPropertyId) {
        this.admin = admin;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.shortBio = shortBio;
        this.deployed = deployed;
        this.tele_Id = tele_Id;
        this.curPropertyId = curPropertyId;
    }

    public int getWorkerId() {
        return workerId;
    }

    public int getSupervisorId() {
        return admin.getAdminId();
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getShortBio() {
        return shortBio;
    }

    public boolean isDeployed() {
        return deployed;
    }

    public String getTele_Id() {
        return tele_Id;
    }

    public int getCurPropertyId() {
        return curPropertyId;
    }

    public void setCurPropertyId(int curPropertyId) {
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

    public void setTele_Id(String tele_Id) {
        this.tele_Id = tele_Id;
    }

}
