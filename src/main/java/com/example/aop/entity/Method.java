package com.example.aop.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "method", uniqueConstraints =
        {@UniqueConstraint(name = "name_class_constraint", columnNames = {"name", "method_class_id"})})
@Data
@NoArgsConstructor
public class Method {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "method_id")
    Integer id;

    @NotBlank
    @Column(name = "name")
    String name;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "method_class_id", nullable = false)
    private MethodClass methodClass;

    public Method(@NotNull MethodClass methodClass, String name) {
        this.methodClass = methodClass;
        this.name = name;
    }
}
