package com.nexushr.backend.controller;
import com.nexushr.backend.model.Employee;
import com.nexushr.backend.model.Payslip;
import com.nexushr.backend.model.SalaryStructure;
import com.nexushr.backend.model.dto.SetSalaryRequest;
import com.nexushr.backend.service.PayrollService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payroll")
@RequiredArgsConstructor
public class PayrollController {
    private final PayrollService payrollService;

    // ADMIN sets/updates an employee's salary structure
    @PostMapping("/salary/{employeeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SalaryStructure> setSalary(@PathVariable Long employeeId,
                                                      @Valid @RequestBody SetSalaryRequest request) {
        return ResponseEntity.ok(payrollService.setSalaryStructure(employeeId, request));
    }

    // ADMIN generates a payslip for a given month/year
    @PostMapping("/generate/{employeeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Payslip> generatePayslip(@PathVariable Long employeeId,
                                                    @RequestParam int month,
                                                    @RequestParam int year) {
        return ResponseEntity.ok(payrollService.generatePayslip(employeeId, month, year));
    }

    // Any logged-in employee can view their own payslips
    @GetMapping("/me")
    public ResponseEntity<List<Payslip>> getMyPayslips(Authentication authentication) {
        Employee employee = (Employee) authentication.getPrincipal();
        return ResponseEntity.ok(payrollService.getMyPayslips(employee.getEmail()));
    }

    // ADMIN/MANAGER can view any employee's payslips
    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<List<Payslip>> getEmployeePayslips(@PathVariable Long employeeId) {
        return ResponseEntity.ok(payrollService.getEmployeePayslips(employeeId));
    }
}
