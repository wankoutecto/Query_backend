package com.example.SQL_Queries.dto;

import lombok.Data;

@Data
public class QueryResponse {
    private Integer id;
    private String question;
    private String answer;
}
