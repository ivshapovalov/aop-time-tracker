package com.example.aop.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Aspect
@Order(1)
@Component
@Slf4j
public class MethodLogger {

    @Pointcut("within(@com.example.aop.annotations.DebugLogging *)")
    public void annotationLoggerPointcut() {
    }

    @AfterThrowing(pointcut = "annotationLoggerPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        Class invokedClass = joinPoint.getSignature().getDeclaringType();
        Logger logger = LoggerFactory.getLogger(invokedClass);
        String methodName = joinPoint.getSignature().getName();
        logger.error("Exception in {}.{}() with cause = {}",
                invokedClass.getName(), methodName,
                e.getCause() != null ? e.getCause() : e.getMessage());
    }

    @Around("annotationLoggerPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Class invokedClass = joinPoint.getSignature().getDeclaringType();
        Logger logger = LoggerFactory.getLogger(invokedClass);
        String methodName = joinPoint.getSignature().getName();
        Map<String, Object> parameters = obtainMethodArguments(joinPoint);
        logger.debug("Enter: {}.{}() with argument[s] = {}",
                invokedClass.getName(), methodName, parameters);
        try {
            Object result = joinPoint.proceed();
            logger.debug("Exit: {}.{}() with result = {}",
                    invokedClass.getName(), methodName,
                    result instanceof Optional value ? value.orElse("Optional{empty}") : result);
            return result;
        } catch (IllegalArgumentException e) {
            logger.error("Illegal argument: {} in {}.{}()",
                    parameters, invokedClass.getName(), methodName);
            throw e;
        }
    }

    private Map<String, Object> obtainMethodArguments(ProceedingJoinPoint joinPoint) {
        Map<String, Object> parameters = new HashMap<>();
        String[] parameterNames = ((CodeSignature) joinPoint.getSignature()).getParameterNames();
        Object[] parameterValues = joinPoint.getArgs();
        for (int i = 0; i < parameterNames.length && i < parameterValues.length; i++) {
            parameters.put(parameterNames[i], parameterValues[i]);
        }
        return parameters;
    }
}
