package com.example.aop.service;

import com.example.aop.annotations.DebugLogging;
import com.example.aop.entity.MethodClass;
import com.example.aop.repository.MethodClassRepository;
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
public class MethodClassService {
    private final static ReadWriteLock lock = new ReentrantReadWriteLock();
    private final MethodClassRepository classRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Locked.Write
    public MethodClass findByName(String className) {
        return tryFindByNameOrCreate(className);
    }

    private MethodClass tryFindByNameOrCreate(String className) {
        Optional<MethodClass> foundMethodClass = classRepository.findByName(className);
        if (foundMethodClass.isEmpty()) {
            return createNewMethodClass(className);
        } else {
            return foundMethodClass.get();
        }
    }

    private MethodClass createNewMethodClass(String className) {
        MethodClass newClass = new MethodClass(className);
        classRepository.saveAndFlush(newClass);
        Utils.sleep(Duration.ofSeconds(1));
        return newClass;
    }
}

