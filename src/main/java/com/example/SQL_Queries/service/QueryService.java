package com.example.SQL_Queries.service;

import com.example.SQL_Queries.dto.QueryDto;
import com.example.SQL_Queries.dto.QueryResponse;
import com.example.SQL_Queries.exception.ResourceNotFoundException;
import com.example.SQL_Queries.mapper.QueryMapper;
import com.example.SQL_Queries.model.MyQuery;
import com.example.SQL_Queries.model.UserLogin;
import com.example.SQL_Queries.repository.QueryRepo;
import com.example.SQL_Queries.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QueryService {
    @Autowired
    QueryMapper queryMapper;
    @Autowired
    UserRepo userRepo;
    @Autowired
    QueryRepo queryRepo;
    @Autowired
    TaskScheduler scheduler;
    public UserLogin getCurrentUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepo.findByUsername(auth.getName())
                .orElseThrow(()-> new ResourceNotFoundException("User is not found"));
    }
    public MyQuery getQueryById(Integer queryId){
        return queryRepo.findById(queryId)
                .orElseThrow(() -> new ResourceNotFoundException("Query is not found"));
    }

    public List<QueryResponse> getAllUndoneQuery(){
        UserLogin userLogged = getCurrentUser();
        List<MyQuery> queryList = queryRepo.findByUserIdWithUndoneAndDoneAtNull(userLogged.getUserId());
        return queryMapper.mappedToListQueryRes(queryList);
    }

    public void addQuery(QueryDto queryDto) {
        UserLogin userLogged = getCurrentUser();
        MyQuery query = queryMapper.mappedToQuery(queryDto, userLogged);
        queryRepo.save(query);
    }

    public void markQueryDoneAndRemove(Integer queryId) {
        MyQuery q = getQueryById(queryId);
        q.setDone(true);
        q.setDoneAt(LocalDateTime.now());
        queryRepo.save(q);
    }

    public void resetQuery(Integer queryId){
        MyQuery q = getQueryById(queryId);
        q.setDone(false);
        q.setDoneAt(null);
        queryRepo.save(q);
    }

    public void resetAllUserQuery(){
        UserLogin userLogged = getCurrentUser();
        List<MyQuery> queryList = queryRepo.findAllByUserLogin_UserId(userLogged.getUserId());
        queryList.forEach(q -> resetQuery(q.getId()));
    }

    public void scheduleQuery(Integer queryId){
        MyQuery q = getQueryById(queryId);
        q.setDone(false);
        q.setDoneAt(LocalDateTime.now());
        queryRepo.save(q);
        LocalDateTime resetTime = q.getDoneAt().plusDays(3);
        //delay is to make sure is scheduled exactly after 3 days even if the app shuts down
        long delay = Duration.between(
                LocalDateTime.now(), resetTime).toMillis();
        if(delay > 0) {
            scheduler.schedule(()-> resetQuery(queryId), Instant.now().plusMillis(delay));
            System.out.println("schedule run");
        }

    }
    //@EventListener better than @PostConstruct in this case
    //Runs after the entire Spring Boot application is fully started
    //Perfect place to reschedule tasks that were lost during shutdown
    @EventListener(ApplicationReadyEvent.class)
    public void reschedulePendingQuery(){
        List<MyQuery> pendingList = queryRepo
                .findByNotDoneAndDoneAtAfter(LocalDateTime.now().minusDays(3));
        pendingList.forEach(query -> scheduleQuery(query.getId()));
    }
}
