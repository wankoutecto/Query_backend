package com.example.SQL_Queries.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class UserLogin {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer userId;
    private String username;
    private String password;
    List<String> roles;
    @OneToMany(mappedBy="userLogin", cascade = CascadeType.ALL, orphanRemoval = true)
    List<MyQuery> queryList;
}
