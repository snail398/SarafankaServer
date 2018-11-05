package com.sarafanka.team.sarafanka.server.repository;

import com.sarafanka.team.sarafanka.server.entity.Establishment;
import com.sarafanka.team.sarafanka.server.entity.Marketolog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface EstablishmentRepository extends JpaRepository<Establishment,Long> {
    @Modifying
    @Query("update Establishment est set est.pathToAvatar = :newPath , est.avatarChangeDate=:date where est.estName = :estname")
    @Transactional
    Integer setPathToAvatar(@Param("estname")String estname, @Param("newPath")String newPath, @Param("date")Long date);


    @Modifying
    @Query("update Establishment est set est.estName = :estname, est.estDescription=:estdescription, est.factAdress=:estaddress, est.estPhone=:estphone, est.estSite=:estsite, est.estEmail=:estemail, est.estWorkTime=:estworktime where est.id = :estid")
    @Transactional
    void setNewInfo(@Param("estid")Long estid,
                            @Param("estname")String estname,
                            @Param("estdescription")String estdescription,
                            @Param("estaddress")String estaddress,
                            @Param("estphone")String estphone,
                            @Param("estsite")String estsite,
                            @Param("estemail")String estemail,
                            @Param("estworktime")String estworktime);


    Establishment findByid(Long id);
    Establishment findByFactAdress(String adress);
    Establishment findByEstName(String estName);

    List<Establishment> findByCompanyID(Long id);
}
