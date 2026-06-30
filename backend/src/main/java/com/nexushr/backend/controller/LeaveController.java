package com.nexushr.backend.controller;
import com.nexushr.backend.model.Employee;
import com.nexushr.backend.model.EmployeeRole;
import com.nexushr.backend.model.LeaveRequest;
import com.nexushr.backend.model.dto.ApplyLeaveRequest;
import com.nexushr.backend.service.LeaveService;
import com.nexushr.backend.model.EmployeeRole;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/leaves")
@RequiredArgsConstructor
public class LeaveController {
    private final LeaveService leaveService;

    // Any logged-in employee can apply for leave
    @PostMapping("/apply")
    public ResponseEntity<LeaveRequest> applyLeave(@Valid @RequestBody ApplyLeaveRequest request,
                                                    Authentication authentication) {
        Employee employee = (Employee) authentication.getPrincipal();
        return ResponseEntity.ok(leaveService.applyLeave(employee.getEmail(), request));
    }

    // Any logged-in employee can view their own leave history
    @GetMapping("/me")
    public ResponseEntity<List<LeaveRequest>> getMyLeaves(Authentication authentication) {
        Employee employee = (Employee) authentication.getPrincipal();
        return ResponseEntity.ok(leaveService.getMyLeaves(employee.getEmail()));
    }

    // ADMIN/MANAGER can view all pending leave requests waiting for approval
    @GetMapping("/pending")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<List<LeaveRequest>> getPendingLeaves() {
        return ResponseEntity.ok(leaveService.getPendingLeaves());
    }

    @PutMapping("/{id}/approve")
public ResponseEntity<?> approveLeave(@PathVariable Long id, Authentication authentication) {
    Employee approver = (Employee) authentication.getPrincipal();
    if (approver.getRole() == EmployeeRole.EMPLOYEE) {
        return ResponseEntity.status(403).body(Map.of("message", "Only ADMIN or MANAGER can approve leaves"));
    }
    return ResponseEntity.ok(leaveService.approveLeave(id, approver.getEmail()));
}

@PutMapping("/{id}/reject")
public ResponseEntity<?> rejectLeave(@PathVariable Long id, Authentication authentication) {
    Employee approver = (Employee) authentication.getPrincipal();
    if (approver.getRole() == EmployeeRole.EMPLOYEE) {
        return ResponseEntity.status(403).body(Map.of("message", "Only ADMIN or MANAGER can reject leaves"));
    }
    return ResponseEntity.ok(leaveService.rejectLeave(id, approver.getEmail()));
}
}
