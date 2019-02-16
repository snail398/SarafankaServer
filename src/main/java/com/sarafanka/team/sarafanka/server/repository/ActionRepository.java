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

    @Modifying
    @Query("update Action action set action.reward = :reward,action.supportReward=:supportReward,action.target = :target, action.description=:description, action.timeStart=:timeStart, action.timeEnd=:timeEnd, action.condition=:condition where action.id =:id")
    @Transactional
    void changeAction(@Param("id")Long id,@Param("reward") String reward, @Param("supportReward")String supportReward, @Param("target") Integer target,@Param("description") String description,@Param("timeStart") Long timeStart,@Param("timeEnd") Long timeEnd,@Param("condition") String condition);

    @Modifying
    @Query("update Action action set action.pathToQRCode = :path where action.id = :id")
    @Transactional
    Integer setPathToQR(@Param("id")Long id, @Param("path")String path);

    @Modifying
    @Query("update Action action set action.pathToPDF = :path where action.id = :id")
    @Transactional
    Integer setPathToPDF(@Param("id")Long id, @Param("path")String path);
}
