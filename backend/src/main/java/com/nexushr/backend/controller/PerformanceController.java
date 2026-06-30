package com.nexushr.backend.controller;
import com.nexushr.backend.model.Employee;
import com.nexushr.backend.model.PerformanceReview;
import com.nexushr.backend.model.dto.CreateGoalsRequest;
import com.nexushr.backend.model.dto.SubmitReviewRequest;
import com.nexushr.backend.service.PerformanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/performance")
@RequiredArgsConstructor
public class PerformanceController {
    private final PerformanceService performanceService;

    // Any logged-in employee can create their own goals entry
    @PostMapping("/goals")
    public ResponseEntity<PerformanceReview> createGoals(@Valid @RequestBody CreateGoalsRequest request,
                                                          Authentication authentication) {
        Employee employee = (Employee) authentication.getPrincipal();
        return ResponseEntity.ok(performanceService.createGoals(employee.getEmail(), request));
    }

    // ADMIN/MANAGER submits feedback + rating for a review
    @PutMapping("/{id}/submit")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<PerformanceReview> submitReview(@PathVariable Long id,
                                                           @Valid @RequestBody SubmitReviewRequest request,
                                                           Authentication authentication) {
        Employee reviewer = (Employee) authentication.getPrincipal();
        return ResponseEntity.ok(performanceService.submitReview(id, reviewer.getEmail(), request));
    }

    // Employee acknowledges their own reviewed feedback
    @PutMapping("/{id}/acknowledge")
    public ResponseEntity<PerformanceReview> acknowledgeReview(@PathVariable Long id,
                                                                Authentication authentication) {
        Employee employee = (Employee) authentication.getPrincipal();
        return ResponseEntity.ok(performanceService.acknowledgeReview(id, employee.getEmail()));
    }

    // Any logged-in employee can view their own review history
    @GetMapping("/me")
    public ResponseEntity<List<PerformanceReview>> getMyReviews(Authentication authentication) {
        Employee employee = (Employee) authentication.getPrincipal();
        return ResponseEntity.ok(performanceService.getMyReviews(employee.getEmail()));
    }

    // ADMIN/MANAGER can view any employee's reviews
    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<List<PerformanceReview>> getEmployeeReviews(@PathVariable Long employeeId) {
        return ResponseEntity.ok(performanceService.getEmployeeReviews(employeeId));
    }
}
