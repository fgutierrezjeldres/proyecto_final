package com.talento_futuro.ProyectoFinal.dto;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import lombok.Data;

@Data
public class SensorDataDTO {
	
	private Integer id;
    private String data;
    private String metric;
    private Long receivedAt;
    private Integer sensorId;

    public String getReceivedAtFormatted() {
        return (receivedAt != null) ? 
            Instant.ofEpochMilli(receivedAt)
                   .atOffset(ZoneOffset.UTC)
                   .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) 
            : null;
    }

}
