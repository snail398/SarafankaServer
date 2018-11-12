package com.sarafanka.team.sarafanka.server.repository;

import com.sarafanka.team.sarafanka.server.entity.CookieAndSession;
import com.sarafanka.team.sarafanka.server.entity.Marketolog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CookieAndSessionRepository extends JpaRepository<CookieAndSession,Long> {
    CookieAndSession findByid(Long id);
    CookieAndSession findByAccountID(Long accountID);
    CookieAndSession findByCookie(String cookie);
    List<CookieAndSession> findAllByCookie(String cookie);

    @Transactional
    void deleteAllByAccountID(Long accountID);

    @Transactional
    void deleteAllByCookie(String cookie);
}
