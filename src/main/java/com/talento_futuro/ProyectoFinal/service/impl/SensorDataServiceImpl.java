package com.talento_futuro.ProyectoFinal.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.talento_futuro.ProyectoFinal.dto.SensorDataDTO;
import com.talento_futuro.ProyectoFinal.dto.mapper.SensorDataMapper;
import com.talento_futuro.ProyectoFinal.entity.Sensor;
import com.talento_futuro.ProyectoFinal.entity.SensorData;
import com.talento_futuro.ProyectoFinal.exception.UnauthorizedException;
import com.talento_futuro.ProyectoFinal.repository.IGenericRepository;
import com.talento_futuro.ProyectoFinal.repository.ISensorRepository;
import com.talento_futuro.ProyectoFinal.service.ISensorDataService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SensorDataServiceImpl extends CRUDServiceImpl<SensorData, Integer> implements ISensorDataService{

    private final IGenericRepository<SensorData, Integer> repository;
    private final SensorDataMapper sensorDataMapper;
    private final ISensorRepository sensorRepository;
   
    @Override
    protected IGenericRepository<SensorData, Integer> getRepository() {
        return repository;
    }

    @Override
    @Transactional
    public List<SensorDataDTO>  registerSensorData(JsonNode jsonNode) {
        
        String apiKey = jsonNode.path("api_key").asText();
        if(!isValidSensorApiKey(apiKey)) {
            throw new UnauthorizedException("Invalid sensor API key");
        }

        List<SensorDataDTO> sensorDataDTOList = processSensorData(jsonNode.get("json_data"), apiKey);
        return sensorDataDTOList.stream()
            .map(sensorDataMapper::toEntity)
            .map(this::save)
            .map(sensorDataMapper::toDTO)
            .collect(Collectors.toList()); 
    }

    @Override
    public boolean isValidSensorApiKey(String sensorApiKey) {
        
        return sensorRepository.findBySensorApiKey(sensorApiKey)
        .map(sensor -> sensorApiKey.equals(sensor.getSensorApiKey())) 
        .orElse(false); 

    }

    private List<SensorDataDTO> processSensorData(JsonNode jsonData, String apiKey) {
        List<SensorDataDTO> sensorDataList = new ArrayList<>();

        Sensor sensor = sensorRepository.findBySensorApiKey(apiKey)
        .orElseThrow(() -> new IllegalArgumentException("No se encontró ningún sensor con la API Key proporcionada: " + apiKey));

        for (JsonNode node : jsonData) {
            Long receivedAt = node.path("datetime").asLong();
            
            node.fields().forEachRemaining(entry -> {
                String fieldName = entry.getKey();  

                if (!fieldName.equals("datetime")) {
                    SensorDataDTO sensorDataDTO = new SensorDataDTO();
                    sensorDataDTO.setReceivedAt(receivedAt);  
                    sensorDataDTO.setMetric(fieldName);  
                    sensorDataDTO.setData(entry.getValue().asText()); 
                    sensorDataDTO.setSensorId(sensor.getId()); 
                    System.out.println(sensorDataDTO.toString());  
                    sensorDataList.add(sensorDataDTO);
                }
            });
        }

        return sensorDataList;
    }

}
