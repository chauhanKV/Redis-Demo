package com.example.SpringDataRedisDemo.repo;



import com.example.SpringDataRedisDemo.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IBranchRepo extends JpaRepository<Branch, Long> {
}
