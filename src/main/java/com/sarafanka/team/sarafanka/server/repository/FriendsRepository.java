package com.sarafanka.team.sarafanka.server.repository;

import com.sarafanka.team.sarafanka.server.entity.Friends;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface FriendsRepository extends JpaRepository<Friends,Long> {

    @Modifying
    @Query("update Friends friend set friend.status = :newstatus  where friend.user1_id = :inviterLgnID and friend.user2_id = :accepterLgnID ")
    @Transactional
    Integer acceptFriendRequest(@Param("inviterLgnID") Long inviterLgnID,@Param("accepterLgnID")Long accepterLgnID, @Param("newstatus") Integer newstatus);
}
