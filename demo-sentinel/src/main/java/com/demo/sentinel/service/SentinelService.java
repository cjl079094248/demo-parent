package com.demo.sentinel.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.stereotype.Service;

@Service
public class SentinelService {
    @SentinelResource(value = "sayHello",blockHandler = "blockHandler",fallback = "fallback")
    public String sayHello(String name) {
        /*try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        return "Hello成功," + name;
    }


    public String blockHandler(String name, BlockException ex) {
        return "限流了," + name;
    }

    public String fallback(String name) {
        return "降级了," + name;
    }
}
