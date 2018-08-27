package com.sarafanka.team.sarafanka.server.repository;

import com.sarafanka.team.sarafanka.server.entity.RunningActions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RunningActionsRepository extends JpaRepository<RunningActions,Long> {

    @Modifying
    @Query("update RunningActions ract set ract.percentOfComplete = :newvalue where ract.id = :id")
    @Transactional
    Integer changeProgress(@Param("id")Long id,@Param("newvalue") Integer newValue);

    @Modifying
    @Query("update RunningActions ract set ract.complited = 1 where ract.id = :id")
    @Transactional
    Integer changeComplitedStatus(@Param("id")Long id);

    void deleteByActionTitleID(Long actiontTitleID);
    List<RunningActions> findByid(Long id);
    RunningActions findByActionTitleIDAndAccountLoginID(Long actionid, Long accountID);
}
