package com.example.SQL_Queries.repository;

import com.example.SQL_Queries.model.MyQuery;
import com.example.SQL_Queries.model.UserLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface QueryRepo extends JpaRepository<MyQuery, Integer> {
    @Query("SELECT q FROM MyQuery q WHERE q.done = FALSE AND q.doneAt > :localDate")
    List<MyQuery> findByNotDoneAndDoneAtAfter(@Param("localDate") LocalDateTime localDate);

    @Query("SELECT q FROM MyQuery q WHERE q.userLogin.userId = :userId AND q.done = FALSE AND q.doneAt IS NULL")
    List<MyQuery> findByUserIdWithUndoneAndDoneAtNull(Integer userId);

    List<MyQuery> findAllByUserLogin_UserId(Integer userId);
}
