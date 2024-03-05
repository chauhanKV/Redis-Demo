package com.example.SpringDataRedisDemo.repo;

import com.example.SpringDataRedisDemo.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IEmployeeRepo extends JpaRepository<Employee, Long> {
    Employee findByEmail(String email);
}
