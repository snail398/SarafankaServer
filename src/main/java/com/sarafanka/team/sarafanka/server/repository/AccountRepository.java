package com.sarafanka.team.sarafanka.server.repository;

import com.sarafanka.team.sarafanka.server.entity.Account;
import com.sarafanka.team.sarafanka.server.entity.Marketolog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface AccountRepository extends JpaRepository<Account,Long> {

    @Modifying
    @Query("update Account account set account.login = :newEmail,account.firstName =:newFirstName,account.secondName =:newSecondName where account.login = :oldEmail")
    @Transactional
    Integer changeInfo( @Param("oldEmail")String oldEmail,@Param("newEmail")String newEmail, @Param("newFirstName")String newFirstName, @Param("newSecondName")String newSecondName);

    @Modifying
    @Query("update Account account set account.password = :newPass where account.login = :email")
    @Transactional
    Integer changePass( @Param("email")String email,@Param("newPass")String newPass);

    @Modifying
    @Query("update Account account set account.pathToAvatar = :newPath , account.avatarChangeDate=:date where account.login = :email")
    @Transactional
    Integer setPathToAvatar( @Param("email")String email,@Param("newPath")String newPath,@Param("date")Long date);


    Account findByid(Long id);
    Account findBylogin(String login);
    Account findByPhoneNumber(String phoneNumber);
}
