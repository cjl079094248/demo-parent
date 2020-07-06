package com.demo.controller;

import com.demo.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/mongo")
public class MongoController {

    @Autowired
    MongoTemplate mongoTemplate;

    @GetMapping("/test")
    public Object test() {
        Query query=new Query(Criteria.where("name").is("cjl"));
        List<UserVo> list = mongoTemplate.find(query, UserVo.class,"UserVo");
        return list;
    }
}