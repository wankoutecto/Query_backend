package com.example.SQL_Queries.mapper;

import com.example.SQL_Queries.dto.UserDto;
import com.example.SQL_Queries.model.UserLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapper {
    @Autowired
    PasswordEncoder passwordEncoder;
    public UserLogin mappedToUser(UserDto userDto){
        UserLogin userLogin = new UserLogin();
        userLogin.setUsername(userDto.getUsername());
        String hashPassword = passwordEncoder.encode(userDto.getPassword());
        userLogin.setPassword(hashPassword);
        List<String> roles = new ArrayList<>();
        roles.add("USER");
        userLogin.setRoles(roles);
        return userLogin;
    }
}
