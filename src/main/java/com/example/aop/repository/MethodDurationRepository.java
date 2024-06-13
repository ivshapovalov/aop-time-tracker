package com.example.aop.repository;

import com.example.aop.dto.MethodStatisticsDto;
import com.example.aop.entity.MethodDuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface MethodDurationRepository extends JpaRepository<MethodDuration, Integer> {
    @Query(value = """
            SELECT
                new com.example.aop.dto.MethodStatisticsDto(
                    md.method.methodClass.name,
                    md.method.name,
                    avg(md.durationMillis),
                    sum(md.durationMillis),
                    count(distinct md.id)
                )
            FROM MethodDuration md
            WHERE
                (:className IS NULL OR md.method.methodClass.name=:className) AND
                (:methodName IS NULL OR md.method.name=:methodName) AND
                (cast(:startDateTimeFrom as timestamp) IS NULL OR md.startTime>=:startDateTimeFrom) AND
                (cast(:startDateTimeTo as timestamp) IS NULL OR md.startTime<=:startDateTimeTo) AND
                (cast(:isAsync as boolean ) IS NULL OR md.isAsync=:isAsync)
            group by md.method.methodClass, md.method
            """)
    List<MethodStatisticsDto> getStatistics(
            @Param("startDateTimeFrom") OffsetDateTime startDateTimeFrom,
            @Param("startDateTimeTo") OffsetDateTime startDateTimeTo,
            @Param("className") String className,
            @Param("methodName") String methodName,
            @Param("isAsync") Boolean isAsync
    );
}
