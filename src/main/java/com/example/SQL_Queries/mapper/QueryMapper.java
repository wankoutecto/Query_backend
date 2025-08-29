package com.example.SQL_Queries.mapper;

import com.example.SQL_Queries.dto.QueryDto;
import com.example.SQL_Queries.dto.QueryResponse;
import com.example.SQL_Queries.model.MyQuery;
import com.example.SQL_Queries.model.UserLogin;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class QueryMapper {
    public MyQuery mappedToQuery(QueryDto queryDto, UserLogin userLogin){
        MyQuery query = new MyQuery();
        query.setQuestion(queryDto.getQuestion());
        query.setAnswer(queryDto.getAnswer());
        query.setDone(false);
        query.setUserLogin(userLogin);
        query.setDoneAt(null);
        return query;
    }

    public QueryResponse mappedToQueryRes(MyQuery query){
        QueryResponse qRes = new QueryResponse();
        qRes.setId(query.getId());
        qRes.setQuestion(query.getQuestion());
        qRes.setAnswer(query.getAnswer());
        return qRes;
    }

    public List<QueryResponse> mappedToListQueryRes(List<MyQuery> queryList) {
        return queryList.stream()
                .map(this::mappedToQueryRes)
                .collect(Collectors.toList());
    }
}
