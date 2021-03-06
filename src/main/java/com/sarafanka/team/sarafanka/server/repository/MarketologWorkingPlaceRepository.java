package com.sarafanka.team.sarafanka.server.repository;

import com.sarafanka.team.sarafanka.server.entity.ActionAccess;
import com.sarafanka.team.sarafanka.server.entity.MarketologWorkingPlace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MarketologWorkingPlaceRepository extends JpaRepository<MarketologWorkingPlace,Long> {
    MarketologWorkingPlace findByid(Long id);
    List<MarketologWorkingPlace>  findMarketologWorkingPlacesByMarketologID(Long id);
    MarketologWorkingPlace findByMarketologID(Long id);
    List<MarketologWorkingPlace> findAllByMarketologID(Long id);
    List<MarketologWorkingPlace> findAllByEstablishmentID(Long id);
}
