package com.example.SQL_Queries.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class MyQuery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String question;
    private String answer;
    private boolean done;
    private LocalDateTime doneAt;
    @ManyToOne
    UserLogin userLogin;
}
