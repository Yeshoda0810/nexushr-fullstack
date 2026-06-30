package com.nexushr.backend.controller;
import com.nexushr.backend.model.dto.AttritionInsightResponse;
import com.nexushr.backend.service.AIInsightService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/insights")
@RequiredArgsConstructor
public class AIInsightController {
    private final AIInsightService aiInsightService;

    // ADMIN/MANAGER can view a single employee's attrition risk
    @GetMapping("/attrition/{employeeId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<AttritionInsightResponse> getAttritionRisk(@PathVariable Long employeeId) {
        return ResponseEntity.ok(aiInsightService.getAttritionRisk(employeeId));
    }

    // ADMIN/MANAGER can view attrition risk for all employees, sorted highest-risk first
    @GetMapping("/attrition")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<List<AttritionInsightResponse>> getAllAttritionRisks() {
        return ResponseEntity.ok(aiInsightService.getAllAttritionRisks());
    }
}
