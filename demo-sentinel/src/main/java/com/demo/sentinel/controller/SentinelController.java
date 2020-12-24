package com.demo.sentinel.controller;

import com.demo.sentinel.service.SentinelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sentinel")
public class SentinelController {
    @Autowired
    private SentinelService sentinelService;

    @RequestMapping(value = "/hello",method = {RequestMethod.GET,RequestMethod.POST})
    public String apiHello(String name){
        return sentinelService.sayHello(name);
    }
}
