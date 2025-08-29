package com.example.SQL_Queries.response;

import lombok.Data;

@Data
public class ApiResponse {
    private Object data;
    private String message;

    public ApiResponse(Object data){
        this.data = data;
    }
    public ApiResponse(Object data, String message){
        this.data = data;
        this.message = message;
    }

}
