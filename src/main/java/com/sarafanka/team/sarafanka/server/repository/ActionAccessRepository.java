package com.sarafanka.team.sarafanka.server.repository;

import com.sarafanka.team.sarafanka.server.entity.ActionAccess;
import com.sarafanka.team.sarafanka.server.entity.Marketolog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActionAccessRepository extends JpaRepository<ActionAccess,Long> {
    ActionAccess findByid(Long id);
    List<ActionAccess> findActionAccessesByEstablishmentID(Long id);
}
