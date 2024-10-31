package com.g4t2project.g4t2project.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adminId;
     
    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<Client> clients;

    private String name;
    private boolean isRoot;

    @OneToMany(mappedBy = "admin",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Worker> workers;

    public Long getAdminId() {
        return adminId;
    }
    public String getName() {
        return name;
    }
    public boolean isRoot() {
        return isRoot;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Worker> getWorkers() {
        return workers;
    }

    public void setWorkers(List<Worker> workers) {
        this.workers = workers;
    }

    public void addWorker(Worker worker) {
        workers.add(worker);
    }

    public void removeWorker(Worker worker) {
        workers.remove(worker);
    }

    // Add a client
    public void addClient(Client client) {
        clients.add(client);
        client.setAdmin(this);  // Set the relationship from Client's side
    }

    // Remove a client
    public void removeClient(Client client) {
        clients.remove(client);
        client.setAdmin(null);  // Break the relationship
    }

    // Update client info
    public void updateClientInfo(Long clientId, Client updatedClient) {
        for (Client client : clients) {
            if (client.getClientId().equals(clientId)) {
                client.setName(updatedClient.getName());      // Update fields
                client.setPhoneNumber(updatedClient.getPhoneNumber());
                client.setEmail(updatedClient.getEmail());
                client.setPreferredWorker(updatedClient.getPreferredWorker());
                client.setProperties(updatedClient.getProperties());
                break;
            }
        }
    }
   
}
