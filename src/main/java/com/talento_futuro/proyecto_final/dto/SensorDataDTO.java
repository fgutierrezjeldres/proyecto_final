package com.talento_futuro.proyecto_final.dto;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class SensorDataDTO {

    private Integer id;

    @NotBlank(message = "Data cannot be empty")
    private String data;

    @NotBlank(message = "Metric cannot be empty")
    private String metric;

    @NotNull(message = "Received date cannot be null")
    @Positive(message = "Received date must be a positive value")
    private Long receivedAt;

    @NotNull(message = "Sensor ID cannot be null")
    private Integer sensorId;

    public String getReceivedAtFormatted() {
        return (receivedAt != null) ? Instant.ofEpochMilli(receivedAt)
                .atOffset(ZoneOffset.UTC)
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                : null;
    }

}
