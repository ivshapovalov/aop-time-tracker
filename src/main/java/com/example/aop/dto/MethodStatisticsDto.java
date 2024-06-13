package com.example.aop.dto;

import java.math.BigDecimal;

public record MethodStatisticsDto(
        String className,
        String methodName,
        BigDecimal durationAvg,
        Long durationTotal,
        Long executionAmount
) {
    public MethodStatisticsDto(String className, String methodName, BigDecimal durationAvg, Long durationTotal, Long executionAmount) {
        this.className = className;
        this.methodName = methodName;
        this.durationAvg = durationAvg;
        this.durationTotal = durationTotal;
        this.executionAmount = executionAmount;
    }

    public MethodStatisticsDto(String className, String methodName,
                               Double durationAvg, Long durationTotal,
                               Long executionAmount) {
        this(className, methodName, BigDecimal.valueOf(durationAvg)
                        .setScale(2, java.math.RoundingMode.HALF_EVEN),
                durationTotal, executionAmount);
    }
}

