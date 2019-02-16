package com.sarafanka.team.sarafanka.server.repository;

import com.sarafanka.team.sarafanka.server.entity.Marketolog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarketologRepository extends JpaRepository<Marketolog,Long> {
    Marketolog findByid(Long id);
    Marketolog findByAccountID(Long id);
    Marketolog findByCompanyIDAndMain(Long id,Integer main);
}
