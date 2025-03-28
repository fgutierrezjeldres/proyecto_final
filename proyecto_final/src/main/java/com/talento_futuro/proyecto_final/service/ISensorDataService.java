package com.talento_futuro.proyecto_final.service;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.talento_futuro.proyecto_final.dto.SensorDataDTO;
import com.talento_futuro.proyecto_final.entity.SensorData;

public interface ISensorDataService extends ICRUDService<SensorData, Integer>{

    List<SensorDataDTO>  registerSensorData(JsonNode jsonNode);
    boolean isValidSensorApiKey(String sensorApiKey);
    List<SensorData> findFilteredData(String companyApiKey, Long from, Long to, List<Integer> sensorIds);

}
