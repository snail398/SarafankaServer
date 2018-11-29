package com.sarafanka.team.sarafanka.server.repository;

import com.sarafanka.team.sarafanka.server.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CouponsRepository extends JpaRepository<Coupon,Long> {
    List<Coupon> findByActionID(Long actionID);
    Coupon findByAccountIDAndActionID(Long accountID,Long actionID);

    @Modifying
    @Query("update Coupon coupon set coupon.pathToQRCode = :path where coupon.id = :id")
    @Transactional
    Integer setPathToQR(@Param("id")Long id, @Param("path")String path);

    @Modifying
    @Query("update Coupon coupon set coupon.pathToSarafunka = :path where coupon.id = :id")
    @Transactional
    Integer setPathToSarafunka(@Param("id")Long id, @Param("path")String path);
}
