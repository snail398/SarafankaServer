package com.sarafanka.team.sarafanka.server.repository;

import com.sarafanka.team.sarafanka.server.entity.StaffOperationHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StaffOperationHistoryRepository extends JpaRepository<StaffOperationHistory,Long> {
        List<StaffOperationHistory> findByStaffAccountID(Long id);

}
