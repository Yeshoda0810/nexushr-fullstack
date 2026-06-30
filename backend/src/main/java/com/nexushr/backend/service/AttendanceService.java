package com.nexushr.backend.service;
import com.nexushr.backend.model.Attendance;
import com.nexushr.backend.model.AttendanceStatus;
import com.nexushr.backend.model.Employee;
import com.nexushr.backend.repository.AttendanceRepository;
import com.nexushr.backend.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;

    public Attendance checkIn(String email) {
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        LocalDate today = LocalDate.now();

        attendanceRepository.findByEmployeeIdAndAttendanceDate(employee.getId(), today)
                .ifPresent(a -> {
                    throw new RuntimeException("Already checked in today");
                });

        Attendance attendance = Attendance.builder()
                .employee(employee)
                .attendanceDate(today)
                .checkInTime(LocalDateTime.now())
                .status(AttendanceStatus.PRESENT)
                .build();

        return attendanceRepository.save(attendance);
    }

    public Attendance checkOut(String email) {
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        LocalDate today = LocalDate.now();

        Attendance attendance = attendanceRepository.findByEmployeeIdAndAttendanceDate(employee.getId(), today)
                .orElseThrow(() -> new RuntimeException("You haven't checked in today"));

        if (attendance.getCheckOutTime() != null) {
            throw new RuntimeException("Already checked out today");
        }

        attendance.setCheckOutTime(LocalDateTime.now());
        return attendanceRepository.save(attendance);
    }

    public List<Attendance> getMyAttendance(String email) {
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        return attendanceRepository.findByEmployeeIdOrderByAttendanceDateDesc(employee.getId());
    }

    public List<Attendance> getAttendanceByDate(LocalDate date) {
        return attendanceRepository.findByAttendanceDateOrderByEmployeeIdAsc(date);
    }
}
