package com.nexushr.backend.model.dto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SetSalaryRequest {
    @NotNull
    @Positive
    private BigDecimal basicSalary;

    private BigDecimal hra;
    private BigDecimal conveyanceAllowance;
    private BigDecimal medicalAllowance;
}
