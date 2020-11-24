package com.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/redis")
public class RedisController {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @RequestMapping("setValue")
    public Object setValue(String key,String value){
        Map<String,Object> obj = new HashMap<>();
        redisTemplate.opsForValue().set(key,value);

        obj.put("msg","success");
        obj.put("code",0);
        return obj;
    }

    @RequestMapping("getValue")
    public Object getValue(String key){
        Map<String,Object> obj = new HashMap<>();

        String data = redisTemplate.opsForValue().get(key);

        obj.put("msg","success");
        obj.put("code",0);
        obj.put("data",data);
        return obj;
    }
}
