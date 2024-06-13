package com.example.aop.controller;

import com.example.aop.dto.FullStatisticsDto;
import com.example.aop.service.GeneratorService;
import com.example.aop.service.MethodDurationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.time.OffsetDateTime;

@RestController
@AllArgsConstructor
public class MainController {

    private MethodDurationService methodDurationService;
    private GeneratorService generatorService;

    @Operation(summary = "Вызов указанного количества случайных методов нескольких сервисов",
            description = "Вызов указанного количества случайных методов нескольких сервисов")
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/generate"
    )
    ResponseEntity<Object> generate(
            @RequestParam(value = "executions", required = false, defaultValue = "100") Long executionCount
    ) throws InvocationTargetException, IllegalAccessException {
        generatorService.generate(executionCount);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Вывод статистики по выполненным методам",
            description = "Вывод статистики по выполненным методам за период в формате json")
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/statistics",
            produces = {"application/json"}
    )
    public FullStatisticsDto getStatistics(
            @Schema(description = "Дата начала периода выполнения методов (в UTC)", example = "2024-06-13T16:54:30Z")
            @RequestParam(value = "startDateTimeFrom", required = false)
            OffsetDateTime startDateTimeFrom,
            @Schema(description = "Дата окончания периода выполнения методов (в UTC)", example = "2024-06-13T16:54:30Z")
            @RequestParam(value = "startDateTimeTo", required = false)
            OffsetDateTime startDateTimeTo,
            @Schema(description = "Полное имя класса (с пакетами)", example = "com.example.aop.service.business.BusinessServiceA")
            @RequestParam(value = "className", required = false)
            String className,
            @Schema(description = "Имя метода", example = "method1 или asyncMethod1")
            @RequestParam(value = "methodName", required = false) String methodName,
            @Schema(description = "Признак асинхронного замера времени выполнения метода", example = "true")
            @RequestParam(value = "isAsync", required = false) Boolean isAsync
    ) {
        return methodDurationService.getStatistics(startDateTimeFrom, startDateTimeTo, className, methodName, isAsync);
    }

}
