package com.example.SpringDataRedisDemo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable = false)
    private Long id;

    @Column(name="name")
    private String name;

    @Column(unique = true)
    private String email;

    // CascadeType.ALL means (save address first before saving the details of employee object)
    @OneToOne(cascade = CascadeType.ALL)
    private Address address;

    // @ManyToOne -> Many employees can be mapped to one branch
    // @JsonIgnoreProperties -> this will not include employeeSet inside Branch response
    @ManyToOne
    @JsonIgnoreProperties("employeeSet")
    private Branch branch;

}
