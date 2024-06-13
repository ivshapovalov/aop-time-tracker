package com.example.aop.dto;

import java.math.BigDecimal;
import java.util.List;

public record FullStatisticsDto(
        BigDecimal durationAvg,
        Long durationTotal,
        Long executionAmount,
        List<ClassStatisticsDto> classStatistics
) {
}

