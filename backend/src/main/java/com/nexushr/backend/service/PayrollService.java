package com.nexushr.backend.service;
import com.nexushr.backend.model.Employee;
import com.nexushr.backend.model.Payslip;
import com.nexushr.backend.model.SalaryStructure;
import com.nexushr.backend.model.dto.SetSalaryRequest;
import com.nexushr.backend.repository.EmployeeRepository;
import com.nexushr.backend.repository.PayslipRepository;
import com.nexushr.backend.repository.SalaryStructureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PayrollService {
    private final SalaryStructureRepository salaryStructureRepository;
    private final PayslipRepository payslipRepository;
    private final EmployeeRepository employeeRepository;

    // ADMIN sets or updates an employee's salary structure
    public SalaryStructure setSalaryStructure(Long employeeId, SetSalaryRequest request) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        SalaryStructure structure = salaryStructureRepository.findByEmployeeId(employeeId)
                .orElse(SalaryStructure.builder().employee(employee).build());

        structure.setEmployee(employee);
        structure.setBasicSalary(request.getBasicSalary());
        if (request.getHra() != null) structure.setHra(request.getHra());
        if (request.getConveyanceAllowance() != null) structure.setConveyanceAllowance(request.getConveyanceAllowance());
        if (request.getMedicalAllowance() != null) structure.setMedicalAllowance(request.getMedicalAllowance());

        return salaryStructureRepository.save(structure);
    }

    // Generates a payslip for a given month/year based on the salary structure
    public Payslip generatePayslip(Long employeeId, int month, int year) {
        if (payslipRepository.findByEmployeeIdAndMonthAndYear(employeeId, month, year).isPresent()) {
            throw new RuntimeException("Payslip already generated for " + month + "/" + year);
        }

        SalaryStructure structure = salaryStructureRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new RuntimeException("Salary structure not set for this employee yet"));

        BigDecimal gross = structure.getBasicSalary()
                .add(structure.getHra())
                .add(structure.getConveyanceAllowance())
                .add(structure.getMedicalAllowance());

        // PF = 12% of basic salary (standard India rule)
        BigDecimal pf = structure.getBasicSalary()
                .multiply(BigDecimal.valueOf(0.12))
                .setScale(2, RoundingMode.HALF_UP);

        // Simplified professional tax slab: flat 200 if gross > 15000, else 0
        BigDecimal professionalTax = gross.compareTo(BigDecimal.valueOf(15000)) > 0
                ? BigDecimal.valueOf(200)
                : BigDecimal.ZERO;

        BigDecimal net = gross.subtract(pf).subtract(professionalTax);

        Payslip payslip = Payslip.builder()
                .employee(structure.getEmployee())
                .month(month)
                .year(year)
                .basicSalary(structure.getBasicSalary())
                .hra(structure.getHra())
                .conveyanceAllowance(structure.getConveyanceAllowance())
                .medicalAllowance(structure.getMedicalAllowance())
                .grossSalary(gross)
                .pfDeduction(pf)
                .professionalTax(professionalTax)
                .netSalary(net)
                .build();

        return payslipRepository.save(payslip);
    }

    public List<Payslip> getMyPayslips(String email) {
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        return payslipRepository.findByEmployeeIdOrderByYearDescMonthDesc(employee.getId());
    }

    public List<Payslip> getEmployeePayslips(Long employeeId) {
        return payslipRepository.findByEmployeeIdOrderByYearDescMonthDesc(employeeId);
    }
}
