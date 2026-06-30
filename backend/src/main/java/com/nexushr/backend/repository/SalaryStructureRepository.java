package com.nexushr.backend.repository;
import com.nexushr.backend.model.SalaryStructure;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
public interface SalaryStructureRepository extends JpaRepository<SalaryStructure, Long> {
    Optional<SalaryStructure> findByEmployeeId(Long employeeId);
}
