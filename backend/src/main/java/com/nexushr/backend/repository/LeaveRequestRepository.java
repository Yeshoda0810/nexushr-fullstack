package com.nexushr.backend.repository;
import com.nexushr.backend.model.LeaveRequest;
import com.nexushr.backend.model.LeaveStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long>{
    List<LeaveRequest> findByEmployeeIdOrderByAppliedAtDesc(Long employeeId);

    List<LeaveRequest> findByStatusOrderByAppliedAtAsc(LeaveStatus status);

    List<LeaveRequest> findByEmployeeIdAndStatus(Long employeeId, LeaveStatus status);
}
