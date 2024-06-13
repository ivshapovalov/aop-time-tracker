package com.example.aop.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "method_class")
@Data
@NoArgsConstructor
public class MethodClass {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "method_class_id")
    Integer id;

    @Column(name = "name", nullable = false, unique = true)
    String name;

    public MethodClass(String name) {
        this.name = name;
    }
}
