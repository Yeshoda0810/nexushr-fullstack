package com.nexushr.backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttritionInsightResponse {
    private Long employeeId;
    private String employeeName;
    private int leaveCount;
    private Integer latestRating;
    private int tenureMonths;
    private double riskScore;
    private String riskLevel; // LOW, MEDIUM, HIGH
}