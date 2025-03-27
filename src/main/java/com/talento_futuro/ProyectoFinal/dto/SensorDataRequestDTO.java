package com.talento_futuro.ProyectoFinal.dto;

import java.util.List;

import lombok.Data;

@Data
public class SensorDataRequestDTO {
    private String apiKey;
    private List<SensorDataDTO> data;

}
