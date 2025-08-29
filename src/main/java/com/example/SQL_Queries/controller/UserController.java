package com.example.SQL_Queries.controller;

import com.example.SQL_Queries.dto.UserDto;
import com.example.SQL_Queries.exception.DuplicateResourceFoundException;
import com.example.SQL_Queries.response.ApiResponse;
import com.example.SQL_Queries.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    UserService userService;
    @PostMapping("register")
    public ResponseEntity<ApiResponse> registerUser(@RequestBody UserDto userRegister) {
        try {
            userService.registerUser(userRegister);
            return ResponseEntity.ok(new ApiResponse(null, "user registered"));
        } catch (DuplicateResourceFoundException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(null, e.getMessage()));
        }
    }

    @PostMapping("login")
    public ResponseEntity<ApiResponse> loginUser(@RequestBody UserDto userDto){
        try {
            String token = userService.loginUser(userDto);
            return ResponseEntity.ok(new ApiResponse(token));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(null, e.getMessage()));
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("add/role/{role}/{userId}")
    public ResponseEntity<ApiResponse> addRole(@PathVariable String role, @PathVariable Integer userId){
        try {
            userService.addRole(role, userId);
            return ResponseEntity.ok(new ApiResponse(null, "role added"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse(null, e.getMessage()));
        }
    }
}
