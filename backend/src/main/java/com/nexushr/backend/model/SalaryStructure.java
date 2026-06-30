package com.nexushr.backend.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "salary_structures")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalaryStructure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false, unique = true)
    private Employee employee;

    @Column(nullable = false)
    private BigDecimal basicSalary;

    @Builder.Default
    private BigDecimal hra = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal conveyanceAllowance = BigDecimal.valueOf(1600);

    @Builder.Default
    private BigDecimal medicalAllowance = BigDecimal.valueOf(1250);
}
