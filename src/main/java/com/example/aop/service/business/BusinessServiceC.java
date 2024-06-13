package com.example.aop.service.business;

import com.example.aop.annotations.DebugLogging;
import com.example.aop.annotations.TrackTime;
import com.example.aop.annotations.TrackTimeAsync;
import com.example.aop.service.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Service
@Slf4j
@DebugLogging
public class BusinessServiceC {

    @TrackTime
    public int method1(String uuid) {
        Utils.someBusinessLogic();
        return 1;
    }

    @TrackTime
    public Object method2(String uuid) {
        Utils.someBusinessLogic();
        return 1;
    }

    @TrackTime
    public List<Object> method3(String uuid) {
        Utils.someBusinessLogic();
        return Collections.emptyList();
    }

    @TrackTimeAsync
    @Async
    public Future<Integer> asyncMethod1(String uuid) {
        Utils.someBusinessLogic();
        return CompletableFuture.completedFuture(10);
    }

    @TrackTimeAsync
    @Async
    public Future<String> asyncMethod2(String uuid) {
        Utils.someBusinessLogic();
        return CompletableFuture.completedFuture("hello world !!!!");
    }

    @TrackTimeAsync
    @Async
    public Future<List<String>> asyncMethod3(String uuid) {
        Utils.someBusinessLogic();
        return CompletableFuture.completedFuture(List.of("hello world !!!!"));
    }
}
