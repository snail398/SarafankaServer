package com.sarafanka.team.sarafanka.server.repository;

import com.sarafanka.team.sarafanka.server.entity.Barmen;
import com.sarafanka.team.sarafanka.server.entity.Marketolog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BarmenRepository extends JpaRepository<Barmen,Long> {
    Barmen findByid(Long id);
    Barmen findByAccountID(Long id);
}
