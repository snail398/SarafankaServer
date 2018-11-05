package com.sarafanka.team.sarafanka.server.repository;

import com.sarafanka.team.sarafanka.server.entity.Invite;
import com.sarafanka.team.sarafanka.server.entity.Marketolog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvitesRepositories extends JpaRepository<Invite,Long> {

   // void deleteInvitesByaction_idAndinit_user_id(Long action_id , Long init_user_id);
   // void deleteByAction_id(Long actionID);
}
