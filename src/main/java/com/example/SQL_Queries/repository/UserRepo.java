package com.example.SQL_Queries.repository;

import com.example.SQL_Queries.model.UserLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<UserLogin, Integer> {
    boolean existsByUsername(String username);
    Optional<UserLogin> findByUsername(String username);
}
