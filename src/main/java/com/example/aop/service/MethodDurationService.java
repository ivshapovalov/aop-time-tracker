package com.example.aop.service;

import com.example.aop.annotations.DebugLogging;
import com.example.aop.dto.ClassStatisticsDto;
import com.example.aop.dto.FullStatisticsDto;
import com.example.aop.dto.MethodStatisticsDto;
import com.example.aop.entity.Method;
import com.example.aop.entity.MethodClass;
import com.example.aop.entity.MethodDuration;
import com.example.aop.repository.MethodDurationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;

@Service
@Slf4j
@DebugLogging
@AllArgsConstructor
public class MethodDurationService {

    private final MethodClassService methodClassService;
    private final MethodService methodService;
    private final MethodDurationRepository durationRepository;

    @Async
    @Transactional
    public void saveDuration(OffsetDateTime startTime, String requestUuid, String className,
                             String methodName, Long durationMillis, Boolean isAsync) {
        MethodClass methodClass = methodClassService.findByName(className);
        Method method = methodService.findByMethodClassAndName(methodClass, methodName);
        MethodDuration duration = new MethodDuration(startTime, requestUuid, method, durationMillis, isAsync);
        durationRepository.saveAndFlush(duration);
    }

    public FullStatisticsDto getStatistics(
            OffsetDateTime filterStartDateTimeFrom, OffsetDateTime filterStartDateTimeTo,
            String filterClassName, String filterMethodName, Boolean filterIsAsync) {

        List<MethodStatisticsDto> methodStatisticsDtoList =
                durationRepository.getStatistics(filterStartDateTimeFrom, filterStartDateTimeTo,
                        filterClassName, filterMethodName, filterIsAsync);

        Map<String, ClassStatisticsDto> classStatistics = methodStatisticsDtoList.stream()
                .collect(groupingBy(MethodStatisticsDto::className, collectingAndThen(toList(), list -> {
                    String className = list.isEmpty() ? "" : list.get(0).className();
                    long executionAmount = list.stream()
                            .collect(summingLong(MethodStatisticsDto::executionAmount));
                    BigDecimal durationAvg = BigDecimal.valueOf(list.stream()
                                    .collect(averagingDouble(methodStatisticsDto -> methodStatisticsDto.durationAvg().doubleValue())))
                            .setScale(2, java.math.RoundingMode.HALF_EVEN);
                    long durationTotal = Math.round(list.stream()
                            .collect(summingLong(MethodStatisticsDto::durationTotal)));
                    return new ClassStatisticsDto(className, durationAvg, durationTotal, executionAmount, list);
                })));

        FullStatisticsDto fullStatisticsDto = classStatistics.values().stream()
                .collect(collectingAndThen(toList(), list -> {
                    long executionAmount = list.stream()
                            .collect(summingLong(ClassStatisticsDto::executionAmount));
                    BigDecimal durationAvg = BigDecimal.valueOf(list.stream()
                                    .collect(averagingDouble(classStatisticsDto -> classStatisticsDto.durationAvg().doubleValue())))
                            .setScale(2, java.math.RoundingMode.HALF_EVEN);
                    long durationTotal = list.stream()
                            .collect(summingLong(ClassStatisticsDto::durationTotal));
                    return new FullStatisticsDto(durationAvg, durationTotal, executionAmount, list);
                }));

        return fullStatisticsDto;
    }
}
