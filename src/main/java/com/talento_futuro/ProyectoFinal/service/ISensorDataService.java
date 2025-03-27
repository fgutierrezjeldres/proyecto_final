package com.talento_futuro.ProyectoFinal.service;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.talento_futuro.ProyectoFinal.dto.SensorDataDTO;
import com.talento_futuro.ProyectoFinal.entity.SensorData;

public interface ISensorDataService extends ICRUDService<SensorData, Integer>{

    List<SensorDataDTO>  registerSensorData(JsonNode jsonNode);
    boolean isValidSensorApiKey(String sensorApiKey);

}
