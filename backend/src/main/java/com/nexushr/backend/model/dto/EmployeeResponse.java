package com.nexushr.backend.model.dto;
import com.nexushr.backend.model.Employee;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import com.nexushr.backend.model.Employee;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeResponse {
    private Long id;
    private String employeeCode;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String role;
    private String departmentName;
    private LocalDate dateOfJoining;
    private boolean active;

    // Converts an Employee entity into a safe response (no password field)
    public static EmployeeResponse fromEntity(Employee employee) {
        return EmployeeResponse.builder()
                .id(employee.getId())
                .employeeCode(employee.getEmployeeCode())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getEmail())
                .phone(employee.getPhone())
                .role(employee.getRole().name())
                .departmentName(employee.getDepartment() != null ? employee.getDepartment().getName() : null)
                .dateOfJoining(employee.getDateOfJoining())
                .active(employee.isActive())
                .build();
    }
}
