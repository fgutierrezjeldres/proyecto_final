package com.talento_futuro.proyecto_final.dto;

import java.util.List;

import lombok.Data;

@Data
public class SensorDataRequestDTO {
    
    private String sensorApiKey;
    private List<SensorDataDTO> data;

}
