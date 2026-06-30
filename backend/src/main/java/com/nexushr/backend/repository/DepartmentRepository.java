package com.nexushr.backend.repository;

import com.nexushr.backend.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    // JpaRepository already gives us save(), findAll(), findById(), deleteById() for free.
}
