package com.example.aop.service;

import com.example.aop.annotations.DebugLogging;
import com.example.aop.entity.Method;
import com.example.aop.entity.MethodClass;
import com.example.aop.repository.MethodRepository;
import lombok.AllArgsConstructor;
import lombok.Locked;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service
@Slf4j
@DebugLogging
@AllArgsConstructor
public class MethodService {
    private final static ReadWriteLock lock = new ReentrantReadWriteLock();
    private final MethodRepository methodRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Locked.Write
    public Method findByMethodClassAndName(MethodClass methodClass, String methodName) {
        return tryFindByMethodClassAndName(methodClass, methodName);
    }

    private Method tryFindByMethodClassAndName(MethodClass methodClass, String methodName) {
        Optional<Method> foundMethod = methodRepository.findByMethodClassIdAndName(methodClass.getId(), methodName);
        if (foundMethod.isEmpty()) {
            return createNewMethod(methodClass, methodName);
        } else {
            return foundMethod.get();
        }
    }

    private Method createNewMethod(MethodClass methodClass, String methodName) {
        Method newMethod = new Method(methodClass, methodName);
        methodRepository.saveAndFlush(newMethod);
        Utils.sleep(Duration.ofSeconds(1));
        return newMethod;
    }
}
