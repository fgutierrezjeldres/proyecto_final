package com.talento_futuro.proyecto_final.messaging;

import java.util.List;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.talento_futuro.proyecto_final.dto.SensorDataDTO;
import com.talento_futuro.proyecto_final.service.ISensorDataService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SensorDataConsumer {

    private final ISensorDataService sensorDataService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${kafka.topic}", groupId = "${kafka.group-id}")
    public void listen(String message) {
        try {
            JsonNode jsonNode = objectMapper.readTree(message);
            System.out.println(" Data kafka: " + jsonNode.toString());
            List<SensorDataDTO> savedData = sensorDataService.registerSensorData(jsonNode);
            System.out.println(" Data saved: " + savedData);
        } catch (Exception e) {
            System.err.println(" Error process data: " + e.getMessage());
        }
    }

}
