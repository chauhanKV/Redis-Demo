package com.example.SpringDataRedisDemo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Setter
@Getter
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    // @OneToMany -> One branch is tagged to many Employees
    // JsonIgnoreProperties -> This ignores branch inside EmployeeSet
    @OneToMany(mappedBy = "branch")
    @JsonIgnoreProperties("branch")
    private Set<Employee> employeeSet;
}
