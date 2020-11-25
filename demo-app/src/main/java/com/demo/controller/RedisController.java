package com.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
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

        System.out.println("log.isTraceEnabled():" + log.isTraceEnabled());
        System.out.println("log.isDebugEnabled():" + log.isDebugEnabled());
        System.out.println("log.isInfoEnabled():" + log.isInfoEnabled());
        System.out.println("log.isWarnEnabled():" + log.isWarnEnabled());
        System.out.println("log.isErrorEnabled():" + log.isErrorEnabled());

        log.info(data);
        return obj;
    }
}
