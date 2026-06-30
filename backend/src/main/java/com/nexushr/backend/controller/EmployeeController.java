package com.nexushr.backend.controller;
import com.nexushr.backend.model.Employee;
import com.nexushr.backend.model.dto.CreateEmployeeRequest;
import com.nexushr.backend.model.dto.EmployeeResponse;
import com.nexushr.backend.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    // ADMIN and MANAGER can view the full employee list
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<List<EmployeeResponse>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    // Any logged-in employee can view their own profile
    @GetMapping("/me")
    public ResponseEntity<EmployeeResponse> getMyProfile(Authentication authentication) {
        Employee currentEmployee = (Employee) authentication.getPrincipal();
        return ResponseEntity.ok(employeeService.getMyProfile(currentEmployee.getEmail()));
    }

    // ADMIN and MANAGER can view any single employee by id
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<EmployeeResponse> getEmployeeById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    // Only ADMIN can directly create an employee (with chosen role)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeResponse> createEmployee(@Valid @RequestBody CreateEmployeeRequest request) {
        return ResponseEntity.ok(employeeService.createEmployee(request));
    }

    // Only ADMIN can update another employee's details/role
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeResponse> updateEmployee(@PathVariable Long id,
                                                            @Valid @RequestBody CreateEmployeeRequest request) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, request));
    }

    // Only ADMIN can deactivate (soft-delete) an employee
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> deactivateEmployee(@PathVariable Long id) {
        employeeService.deactivateEmployee(id);
        return ResponseEntity.ok(Map.of("message", "Employee deactivated successfully"));
    }
}
