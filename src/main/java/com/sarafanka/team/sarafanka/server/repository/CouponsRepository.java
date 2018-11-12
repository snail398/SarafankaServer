package com.sarafanka.team.sarafanka.server.repository;

import com.sarafanka.team.sarafanka.server.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CouponsRepository extends JpaRepository<Coupon,Long> {
    List<Coupon> findByActionID(Long actionID);
}
