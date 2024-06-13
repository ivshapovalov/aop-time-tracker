package com.example.aop;

import lombok.AllArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAspectJAutoProxy
@AllArgsConstructor
@EnableAsync
public class AopTimeTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AopTimeTrackerApplication.class, args);
    }

}
