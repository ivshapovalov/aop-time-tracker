package com.example.aop.aspect;

import com.example.aop.service.MethodDurationService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.concurrent.CompletableFuture;

@Aspect
@Order(2)
@Component
@Slf4j
public class MethodTimeTracker {

    private final MethodDurationService methodDurationService;

    public MethodTimeTracker(MethodDurationService methodDurationService) {
        this.methodDurationService = methodDurationService;
    }

    @Pointcut("@annotation(com.example.aop.annotations.TrackTime)")
    public void annotationTrackTimePointcut() {
    }

    @Pointcut("@annotation(com.example.aop.annotations.TrackTimeAsync) " +
            "&& @annotation(org.springframework.scheduling.annotation.Async)")
    public void annotationTrackTimeAsyncPointcut() {
    }

    @Around("annotationTrackTimePointcut()")
    public Object trackTime(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        OffsetDateTime now = OffsetDateTime.now();
        Long startTime = now.toInstant().toEpochMilli();
        Object result = joinPoint.proceed();
        Long finishTime = System.currentTimeMillis();
        String requestUuid = (String) joinPoint.getArgs()[0];
        methodDurationService.saveDuration(now, requestUuid, className, methodName, finishTime - startTime, false);
        return result;
    }

    @Around("annotationTrackTimeAsyncPointcut()")
    public Object trackTimeAsync(ProceedingJoinPoint joinPoint) {
        OffsetDateTime now = OffsetDateTime.now();
        Long startTime = now.toInstant().toEpochMilli();
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        return CompletableFuture.supplyAsync(() -> {
            try {
                Object result = joinPoint.proceed();
                Long finishTime = System.currentTimeMillis();
                String requestUuid = (String) joinPoint.getArgs()[0];
                methodDurationService.saveDuration(now, requestUuid, className, methodName, finishTime - startTime, true);
                return result;
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        });
    }
}
