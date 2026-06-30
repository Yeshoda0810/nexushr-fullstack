package com.nexushr.backend.service;
import com.nexushr.backend.model.Employee;
import com.nexushr.backend.model.PerformanceReview;
import com.nexushr.backend.model.dto.AttritionInsightResponse;
import com.nexushr.backend.repository.EmployeeRepository;
import com.nexushr.backend.repository.LeaveRequestRepository;
import com.nexushr.backend.repository.PerformanceReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AIInsightService {
    private final EmployeeRepository employeeRepository;
    private final LeaveRequestRepository leaveRequestRepository;
    private final PerformanceReviewRepository performanceReviewRepository;

    // Rule-based (explainable) attrition risk scoring.
    // Not a trained ML model - a transparent heuristic based on 3 signals:
    // leave frequency, latest performance rating, and tenure.
    public AttritionInsightResponse getAttritionRisk(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        int leaveCount = leaveRequestRepository.findByEmployeeIdOrderByAppliedAtDesc(employeeId).size();

        Integer latestRating = performanceReviewRepository.findByEmployeeIdOrderByReviewYearDesc(employeeId)
                .stream()
                .filter(r -> r.getRating() != null)
                .map(PerformanceReview::getRating)
                .findFirst()
                .orElse(null);

        LocalDateTime joinReference = employee.getDateOfJoining() != null
                ? employee.getDateOfJoining().atStartOfDay()
                : employee.getCreatedAt();
        int tenureMonths = (int) ChronoUnit.MONTHS.between(joinReference, LocalDateTime.now());

        double score = 30; // baseline risk
        score += leaveCount * 8;                                  // more leaves -> higher risk
        score -= (latestRating != null ? latestRating * 10 : 0);  // higher rating -> lower risk
        score -= Math.min(tenureMonths * 0.8, 20);                 // longer tenure -> lower risk (capped)

        score = Math.max(0, Math.min(100, score)); // clamp 0-100

        String level = score >= 60 ? "HIGH" : score >= 35 ? "MEDIUM" : "LOW";

        return AttritionInsightResponse.builder()
                .employeeId(employee.getId())
                .employeeName(employee.getFirstName() + " " + employee.getLastName())
                .leaveCount(leaveCount)
                .latestRating(latestRating)
                .tenureMonths(tenureMonths)
                .riskScore(Math.round(score * 10) / 10.0)
                .riskLevel(level)
                .build();
    }

    public List<AttritionInsightResponse> getAllAttritionRisks() {
        return employeeRepository.findAll().stream()
                .filter(Employee::isActive)
                .map(e -> getAttritionRisk(e.getId()))
                .sorted(Comparator.comparingDouble(AttritionInsightResponse::getRiskScore).reversed())
                .toList();
    }
}
