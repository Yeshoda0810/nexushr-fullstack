package com.nexushr.backend.controller;

import com.nexushr.backend.model.Department;
import com.nexushr.backend.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    @Autowired
    private DepartmentRepository departmentRepository;

    // GET http://localhost:8080/api/departments
    @GetMapping
    public List<Department> getAll() {
        return departmentRepository.findAll();
    }

    // POST http://localhost:8080/api/departments
    // body: { "name": "Engineering", "code": "ENG" }
    @PostMapping
    public Department create(@RequestBody Department department) {
        return departmentRepository.save(department);
    }
}
