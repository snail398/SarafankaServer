package com.sarafanka.team.sarafanka.server.repository;

import com.sarafanka.team.sarafanka.server.entity.Referals;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ReferalsRepository extends JpaRepository<Referals,Long> {
    Referals findById(Long id);
    Referals findByAccountID(Long id);
    Referals findByRactID(Long id);
    Referals findByRactIDAndAccountIDAndBonusReceived(Long ractid,Long accid,Integer bonusRecived);

    @Modifying
    @Query("update Referals ref set ref.bonusReceived = 1 where ref.id = :id")
    @Transactional
    Integer receiveBonus( @Param("id")Long id);

    @Modifying
    @Query("update Referals ref set ref.receivingDate = :date where ref.id = :id")
    @Transactional
    Integer setReceivingDate( @Param("id")Long id,@Param("date")Long date);
}
