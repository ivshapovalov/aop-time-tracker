package com.example.aop.repository;

import com.example.aop.entity.MethodClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MethodClassRepository extends JpaRepository<MethodClass, Integer> {

    Optional<MethodClass> findByName(String name);

}

