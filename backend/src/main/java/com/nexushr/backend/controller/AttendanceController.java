package com.nexushr.backend.controller;
import com.nexushr.backend.model.Attendance;
import com.nexushr.backend.model.Employee;
import com.nexushr.backend.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {
    private final AttendanceService attendanceService;

    // Any logged-in employee can check themselves in
    @PostMapping("/check-in")
    public ResponseEntity<Attendance> checkIn(Authentication authentication) {
        Employee employee = (Employee) authentication.getPrincipal();
        return ResponseEntity.ok(attendanceService.checkIn(employee.getEmail()));
    }

    // Any logged-in employee can check themselves out
    @PostMapping("/check-out")
    public ResponseEntity<Attendance> checkOut(Authentication authentication) {
        Employee employee = (Employee) authentication.getPrincipal();
        return ResponseEntity.ok(attendanceService.checkOut(employee.getEmail()));
    }

    // Any logged-in employee can view their own attendance history
    @GetMapping("/me")
    public ResponseEntity<List<Attendance>> getMyAttendance(Authentication authentication) {
        Employee employee = (Employee) authentication.getPrincipal();
        return ResponseEntity.ok(attendanceService.getMyAttendance(employee.getEmail()));
    }

    // ADMIN/MANAGER can view everyone's attendance for a given date
    @GetMapping("/by-date")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<List<Attendance>> getAttendanceByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(attendanceService.getAttendanceByDate(date));
    }
}
