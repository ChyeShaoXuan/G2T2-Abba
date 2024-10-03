package com.g4t2project.g4t2project.entity;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "admin")
public class Admin extends Employee {

    @OneToMany(mappedBy = "directSupervisor")
    private List<Worker> underSupervision = new ArrayList<>();

    // Methods for adding/removing workers, approving/rejecting leave applications

    public List<Worker> getUnderSupervision() {
        return underSupervision;
    }

    public void setUnderSupervision(List<Worker> underSupervision) {
        this.underSupervision = underSupervision;
    }

    public void addWorker(Worker worker) {
        underSupervision.add(worker);
        worker.setDirectSupervisor(this.getName());
    }

    public void removeWorker(Worker worker) {
        underSupervision.remove(worker);
    }

    // Additional methods from AdminService can be included here if desired.
}