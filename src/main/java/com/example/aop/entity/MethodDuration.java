package com.example.aop.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Entity
@Table(name = "method_duration")
@Data
@NoArgsConstructor
public class MethodDuration {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "duration_id")
    Integer id;

    @NotNull
    @Column(name = "start_time")
    OffsetDateTime startTime;
    @NotNull
    @Positive
    @Column(name = "duration_millis")
    Long durationMillis;
    @NotNull
    @Column(name = "async")
    Boolean isAsync;
    @NotNull
    @Column(name = "request_uuid")
    private String requestUuid;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "method_id", nullable = false)
    private Method method;

    public MethodDuration(@NotNull OffsetDateTime startTime, @NotNull String requestUuid,
                          @NotNull Method method, @NotNull Long durationMillis,
                          @NotNull Boolean isAsync) {
        this.startTime = startTime;
        this.requestUuid = requestUuid;
        this.method = method;
        this.durationMillis = durationMillis;
        this.isAsync = isAsync;
    }
}
