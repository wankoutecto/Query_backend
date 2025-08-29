package com.example.SQL_Queries.security;

import com.example.SQL_Queries.exception.ResourceNotFoundException;
import com.example.SQL_Queries.model.UserLogin;
import com.example.SQL_Queries.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsServiceCustom implements UserDetailsService {
    @Autowired
    UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserLogin userLogin = userRepo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("can't load user. user does not exist"));
        return User.withUsername(userLogin.getUsername())
                .password(userLogin.getPassword())
                .roles(userLogin.getRoles().toArray(new String[0]))
                .build();
    }
}
