package com.g4t2project.g4t2project.entity;
import jakarta.persistence.*;
import java.util.*;
@Entity
public class Admin extends Employee {
    private ArrayList<Worker> under_supervision;
    

}
