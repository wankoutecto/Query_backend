package com.example.SQL_Queries.controller;

import com.example.SQL_Queries.dto.QueryDto;
import com.example.SQL_Queries.dto.QueryResponse;
import com.example.SQL_Queries.exception.DuplicateResourceFoundException;
import com.example.SQL_Queries.exception.ResourceNotFoundException;
import com.example.SQL_Queries.response.ApiResponse;
import com.example.SQL_Queries.service.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("query")
public class QueryController {
    @Autowired
    QueryService qservice;

    @GetMapping("get/all")
    public ResponseEntity<ApiResponse> getAllUndoneQuery(){
        try {
            List<QueryResponse> qRes = qservice.getAllUndoneQuery();
            return ResponseEntity.ok(new ApiResponse(qRes, null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(null, e.getMessage()));
        }
    }


    @PostMapping("reset/all")
    public ResponseEntity<ApiResponse> resetAllUserQuery(){
        try {
            qservice.resetAllUserQuery();
            return ResponseEntity.ok(new ApiResponse(null, "All query reset"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(null, e.getMessage()));
        }
    }

    @PostMapping("add")
    public ResponseEntity<ApiResponse> addQuery(@RequestBody QueryDto query){
        try {
            qservice.addQuery(query);
            return ResponseEntity.ok(new ApiResponse(null, "Query added"));
        } catch (DuplicateResourceFoundException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(null, e.getMessage()));
        }
    }

    @PostMapping("done/{queryId}")
    public ResponseEntity<ApiResponse> markQueryDone(@PathVariable Integer queryId){
        try {
            System.out.println(queryId);
            qservice.markQueryDoneAndRemove(queryId);
            return ResponseEntity.ok(new ApiResponse(null, "Query Done"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(null, e.getMessage()));
        }
    }

    @PostMapping("schedule/{queryId}")
    public ResponseEntity<ApiResponse> scheduleQuery(@PathVariable Integer queryId){
        try {
            qservice.scheduleQuery(queryId);
            return ResponseEntity.ok(new ApiResponse(null, "Query Scheduled"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(null, e.getMessage()));
        }
    }
}
