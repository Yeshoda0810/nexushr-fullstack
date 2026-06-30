package com.nexushr.backend.service;
import com.nexushr.backend.model.*;
import com.nexushr.backend.model.dto.ApplyLeaveRequest;
import com.nexushr.backend.repository.EmployeeRepository;
import com.nexushr.backend.repository.LeaveRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaveService {
   private final LeaveRequestRepository leaveRequestRepository;
    private final EmployeeRepository employeeRepository;

    public LeaveRequest applyLeave(String email, ApplyLeaveRequest request) {
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new RuntimeException("End date cannot be before start date");
        }

        LeaveRequest leave = LeaveRequest.builder()
                .employee(employee)
                .leaveType(request.getLeaveType())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .reason(request.getReason())
                .status(LeaveStatus.PENDING)
                .build();

        return leaveRequestRepository.save(leave);
    }

    public List<LeaveRequest> getMyLeaves(String email) {
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        return leaveRequestRepository.findByEmployeeIdOrderByAppliedAtDesc(employee.getId());
    }

    public List<LeaveRequest> getPendingLeaves() {
        return leaveRequestRepository.findByStatusOrderByAppliedAtAsc(LeaveStatus.PENDING);
    }

    public LeaveRequest approveLeave(Long leaveId, String approverEmail) {
        return updateLeaveStatus(leaveId, approverEmail, LeaveStatus.APPROVED);
    }

    public LeaveRequest rejectLeave(Long leaveId, String approverEmail) {
        return updateLeaveStatus(leaveId, approverEmail, LeaveStatus.REJECTED);
    }

    private LeaveRequest updateLeaveStatus(Long leaveId, String approverEmail, LeaveStatus newStatus) {
        LeaveRequest leave = leaveRequestRepository.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));

        if (leave.getStatus() != LeaveStatus.PENDING) {
            throw new RuntimeException("This leave request has already been " + leave.getStatus());
        }

        Employee approver = employeeRepository.findByEmail(approverEmail)
                .orElseThrow(() -> new RuntimeException("Approver not found"));

        leave.setStatus(newStatus);
        leave.setApprovedBy(approver);

        return leaveRequestRepository.save(leave);
    }

    public long calculateLeaveDays(LeaveRequest leave) {
        return ChronoUnit.DAYS.between(leave.getStartDate(), leave.getEndDate()) + 1;
    } 
}
