package com.example.aop.service;

import com.example.aop.annotations.DebugLogging;
import com.example.aop.service.business.BusinessServiceA;
import com.example.aop.service.business.BusinessServiceB;
import com.example.aop.service.business.BusinessServiceC;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
@DebugLogging
public class GeneratorService {

    private final BusinessServiceA businessServiceA;
    private final BusinessServiceB businessServiceB;
    private final BusinessServiceC businessServiceC;

    public void generate(long executionCount) throws InvocationTargetException, IllegalAccessException {
        List<String> methodNames = List.of("method1", "method2", "method3", "asyncMethod1", "asyncMethod2", "asyncMethod3");
        List<InstanceMethod> allMethods = List.of(businessServiceA, businessServiceB, businessServiceC).stream()
                .flatMap(service -> methodNames.stream()
                        .map(methodName -> ReflectionUtils.findMethod(service.getClass(), methodName, String.class))
                        .map(method -> new InstanceMethod(service, method)))
                .collect(Collectors.toList());
        for (int i = 0; i < executionCount; i++) {
            String requestUuid = UUID.randomUUID().toString();
            int index = getRandomNumber(0, allMethods.size() - 1);
            Object instance = allMethods.get(index).instance;
            Method method = allMethods.get(index).method;
            method.invoke(instance, requestUuid);
        }
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    private record InstanceMethod(Object instance, Method method) {
    }
}
