package com.g4t2project.g4t2project.entity;
import jakarta.persistence.*;
@Entity
@Table(name = "leave_application")
public class leaveApplication {
   
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "worker_id", nullable = false)
    private Worker worker;

    @Column(name = "status")
    private String status;

    public void setId(Long id) {
        this.id = id;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    
}
