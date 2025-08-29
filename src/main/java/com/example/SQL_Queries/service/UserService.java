package com.example.SQL_Queries.service;

import com.example.SQL_Queries.dto.UserDto;
import com.example.SQL_Queries.exception.DuplicateResourceFoundException;
import com.example.SQL_Queries.exception.ResourceNotFoundException;
import com.example.SQL_Queries.mapper.UserMapper;
import com.example.SQL_Queries.model.UserLogin;
import com.example.SQL_Queries.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserRepo userRepo;
    @Autowired
    UserMapper userMapper;
    @Autowired
    AuthenticationManager authManager;
    @Autowired
    JwtService jwtService;

    public void registerUser(UserDto userRegister) {
        UserLogin userLogin = userMapper.mappedToUser(userRegister);
        if(userRepo.existsByUsername(userLogin.getUsername())){
           throw new DuplicateResourceFoundException("Username already exists");
        }
        userRepo.save(userLogin);
    }

    public String loginUser(UserDto userDto) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword())
        );
        UserDetails userDetails = (UserDetails) auth.getPrincipal();

        return jwtService.generateToken(userDetails);
    }

    public void addRole(String role, Integer userId){
        UserLogin userLogged = userRepo.findById(userId)
                .orElseThrow(()->new ResourceNotFoundException("User not found"));
        boolean exists = userLogged.getRoles()
                .stream()
                .anyMatch(r -> r.equalsIgnoreCase(role));
        if(exists){
            throw new DuplicateResourceFoundException("Role already exists");
        }
        userLogged.getRoles().add(role.toUpperCase());
        userRepo.save(userLogged);
    }
}
