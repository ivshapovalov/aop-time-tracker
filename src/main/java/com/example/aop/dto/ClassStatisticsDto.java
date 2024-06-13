package com.example.aop.dto;

import java.math.BigDecimal;
import java.util.List;

public record ClassStatisticsDto(
        String className,
        BigDecimal durationAvg,
        Long durationTotal,
        Long executionAmount,
        List<MethodStatisticsDto> methodStatistics
) {
}

