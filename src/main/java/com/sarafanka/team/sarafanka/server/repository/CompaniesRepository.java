package com.sarafanka.team.sarafanka.server.repository;

import com.sarafanka.team.sarafanka.server.entity.Action;
import com.sarafanka.team.sarafanka.server.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface CompaniesRepository extends JpaRepository<Company,Long> {
    @Modifying
    @Query("update Company company set company.title = :newName,company.category =:newCategory,company.companyType =:newType,company.description =:newDescription,company.adress=:newAdress,company.phone =:newPhone,company.site =:newSite,company.inn=:inn,company.kpp=:kpp,company.ogrn=:ogrn where company.id = :id")
    @Transactional
    Integer changeCompanyInfo(@Param("id")Long id, @Param("newName")String newName,@Param("newCategory")String newCategory,@Param("newType")String newType,@Param("newDescription")String newDescription, @Param("newAdress")String newAdress,@Param("newPhone")String newPhone,@Param("newSite")String newSite,@Param("inn")String inn,@Param("kpp")String kpp,@Param("ogrn")String ogrn);

    @Modifying
    @Query("update  Company company set company.pathToAvatar = :newPath , company.avatarChangeDate=:date where company.id = :id")
    @Transactional
    Integer setPathToAvatar( @Param("id")Long id,@Param("newPath")String newPath,@Param("date")Long date);


    Company findById(Long id);
    Company findByTitle(String title);
}
