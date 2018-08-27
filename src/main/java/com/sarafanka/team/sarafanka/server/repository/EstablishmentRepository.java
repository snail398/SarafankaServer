package com.sarafanka.team.sarafanka.server.repository;

import com.sarafanka.team.sarafanka.server.entity.Establishment;
import com.sarafanka.team.sarafanka.server.entity.Marketolog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EstablishmentRepository extends JpaRepository<Establishment,Long> {
    Establishment findByid(Long id);
    Establishment findByFactAdress(String adress);


    List<Establishment> findByCompanyID(Long id);
}
