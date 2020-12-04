package com.demo.retrieve;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class RetrieveApplication {

    public static void main(String[] args) {
        SpringApplication.run(RetrieveApplication.class, args);
    }
}
