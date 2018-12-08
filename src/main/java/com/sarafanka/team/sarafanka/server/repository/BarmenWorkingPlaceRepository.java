package com.sarafanka.team.sarafanka.server.repository;

import com.sarafanka.team.sarafanka.server.entity.BarmenWorkingPlace;
import com.sarafanka.team.sarafanka.server.entity.MarketologWorkingPlace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BarmenWorkingPlaceRepository extends JpaRepository<BarmenWorkingPlace,Long> {
    BarmenWorkingPlace findByid(Long id);
    List<BarmenWorkingPlace>  findBarmenWorkingPlacesByBarmenID(Long id);
    BarmenWorkingPlace findByBarmenID(Long id);
    List<BarmenWorkingPlace> findAllByBarmenID(Long id);
    List<BarmenWorkingPlace> findAllByEstablishmentID(Long id);
}
