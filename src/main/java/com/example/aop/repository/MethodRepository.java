package com.example.aop.repository;

import com.example.aop.entity.Method;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MethodRepository extends JpaRepository<Method, Integer> {

    Optional<Method> findByMethodClassIdAndName(Integer methodClassId, String name);

}

