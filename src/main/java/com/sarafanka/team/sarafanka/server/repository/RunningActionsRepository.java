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

    void deleteRunningActionsByActionTitleID(Long actiontTitleID);
    RunningActions findByid(Long id);
    RunningActions findByActionTitleIDAndAccountLoginIDAndComplited(Long actionid, Long accountID,Integer complited);

    List<RunningActions> findByActionTitleIDAndComplited(Long actionID,Integer complited);
    List<RunningActions> findByActionTitleID(Long actionID);
    List<RunningActions> findAllByComplited(Integer complited);
    List<RunningActions> findAllByAccountLoginID(Long accountID);

    @Modifying
    @Query("update RunningActions ract set ract.complited = -1 where ract.id = :id")
    @Transactional
    Integer deleteRActByUser(@Param("id")Long id);

    @Modifying
    @Query("update RunningActions ract set ract.complited = -2 where ract.id = :id")
    @Transactional
    Integer setUsedStatusByUser(@Param("id")Long id);


    @Modifying
    @Query("update RunningActions ract set ract.pathToQRCode = :path where ract.id = :id")
    @Transactional
    Integer setPathToQR(@Param("id")Long id, @Param("path")String path);

    @Modifying
    @Query("update RunningActions ract set ract.pathToSarafunkaForFriend = :path where ract.id = :id")
    @Transactional
    Integer setPathToSarafunka(@Param("id")Long id, @Param("path")String path);

    @Modifying
    @Query("update RunningActions ract set ract.repostCountAndroid = ract.repostCountAndroid+1  where ract.id = :id")
    @Transactional
    Integer increaseRepostAndroid(@Param("id")Long id);

    @Modifying
    @Query("update RunningActions ract set ract.repostCountDownload = ract.repostCountDownload+1  where ract.id = :id")
    @Transactional
    Integer increaseRepostDownload(@Param("id")Long id);

    @Modifying
    @Query("update RunningActions ract set ract.repostCountFB = ract.repostCountFB+1  where ract.id = :id")
    @Transactional
    Integer increaseRepostFB(@Param("id")Long id);

    @Modifying
    @Query("update RunningActions ract set ract.repostCountTW = ract.repostCountTW+1  where ract.id = :id")
    @Transactional
    Integer increaseRepostTW(@Param("id")Long id);

    @Modifying
    @Query("update RunningActions ract set ract.repostCountVK = ract.repostCountVK+1  where ract.id = :id")
    @Transactional
    Integer increaseRepostVK(@Param("id")Long id);

    @Modifying
    @Query("update RunningActions ract set ract.repostCountWA = ract.repostCountWA+1  where ract.id = :id")
    @Transactional
    Integer increaseRepostWA(@Param("id")Long id);


    @Modifying
    @Query("update RunningActions ract set ract.pdfUsagesCount = ract.pdfUsagesCount+1  where ract.pathToSarafunkaForFriend = :path")
    @Transactional
    Integer increasePdfUsages(@Param("path")String path);
}
