package com.sarafanka.team.sarafanka.server.repository;

import com.sarafanka.team.sarafanka.server.entity.Invite;
import com.sarafanka.team.sarafanka.server.entity.Marketolog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvitesRepositories extends JpaRepository<Invite,Long> {

}
