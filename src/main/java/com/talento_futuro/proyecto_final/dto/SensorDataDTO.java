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
    @NotBlank(message = "Los datos no pueden estar vacíos")
    private String data;

    @NotBlank(message = "La métrica no puede estar vacía")
    private String metric;

    @NotNull(message = "La fecha de recepción no puede ser nula")
    @Positive(message = "La fecha de recepción debe ser un valor positivo")
    private Long receivedAt;

    @NotNull(message = "El ID del sensor no puede ser nulo")
    private Integer sensorId;

    public String getReceivedAtFormatted() {
        return (receivedAt != null) ? 
            Instant.ofEpochMilli(receivedAt)
                   .atOffset(ZoneOffset.UTC)
                   .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) 
            : null;
    }

}
