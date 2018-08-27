package com.sarafanka.team.sarafanka.server.repository;

import com.sarafanka.team.sarafanka.server.entity.Action;
import com.sarafanka.team.sarafanka.server.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ActionRepository extends JpaRepository<Action,Long> {

    @Modifying
    @Query("update Action action set action.peopleUsed = action.peopleUsed+1 where action.id = :id")
    @Transactional
    Integer increasePeopleUser(@Param("id")Long id);


    Action findById(Long id);
    Action findByRewardAndDescription(String reward, String description);
    List<Action> findActionsByOrganizationID(Long id);
}
