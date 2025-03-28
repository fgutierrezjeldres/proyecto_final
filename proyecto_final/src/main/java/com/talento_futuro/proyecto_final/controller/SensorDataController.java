package com.talento_futuro.proyecto_final.controller;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.talento_futuro.proyecto_final.dto.SensorDataDTO;
import com.talento_futuro.proyecto_final.mapper.SensorDataMapper;
import com.talento_futuro.proyecto_final.entity.SensorData;
import com.talento_futuro.proyecto_final.service.ISensorDataService;


import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/sensor_data")
@RequiredArgsConstructor
public class SensorDataController {

    private final ISensorDataService sensorDataService;
    private final SensorDataMapper sensorDataMapper;

    @PostMapping
    public ResponseEntity<List<SensorDataDTO>> insertSensorData(@RequestBody JsonNode data) {
        List<SensorDataDTO>  savedSensorData = sensorDataService.registerSensorData(data);
        String ids = savedSensorData.stream()
        .map(sensor -> String.valueOf(sensor.getId()))
        .collect(Collectors.joining(","));
       

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .queryParam("status", "created")
                .buildAndExpand(ids)
                .toUri();

        return ResponseEntity.created(location).body(savedSensorData);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SensorDataDTO> getSensorDataById(@PathVariable Integer id) {
        SensorData sensorData = sensorDataService.findById(id);
        SensorDataDTO sensorDataDTO = sensorDataMapper.toDTO(sensorData);

        URI uri = ServletUriComponentsBuilder
                
                .fromCurrentRequest()
                .path("/{id}")
                .queryParam("status", "fetched")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.ok()
                             .location(uri)
                             .body(sensorDataDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SensorDataDTO> updateSensorData(@PathVariable Integer id, @RequestBody SensorDataDTO sensorDataDTO) {
        SensorData sensorData = sensorDataMapper.toEntity(sensorDataDTO);
        SensorData updatedSensorData = sensorDataService.update(sensorData, id);
        SensorDataDTO updatedSensorDataDTO = sensorDataMapper.toDTO(updatedSensorData);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .queryParam("status", "updated")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.ok()
                             .location(uri)
                             .body(updatedSensorDataDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSensorData(@PathVariable Integer id) {
        sensorDataService.delete(id);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .queryParam("status", "deleted")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.noContent()
                             .location(uri)
                             .build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<SensorDataDTO>> getAllSensorData() {
        List<SensorData> sensorDataList = sensorDataService.findAll();
        List<SensorDataDTO> sensorDataDTOs = sensorDataList.stream()
                                                           .map(sensorDataMapper::toDTO)
                                                           .collect(Collectors.toList());

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/all")
                .queryParam("status", "fetched")
                .build()
                .toUri();

        return ResponseEntity.ok()
                             .location(uri)
                             .body(sensorDataDTOs);
    }

    @GetMapping
    public ResponseEntity<List<SensorDataDTO>> getFilteredSensorData(
            @RequestHeader(value = "company_api_key", required = false) String companyApiKeyHeader,
            @RequestParam(value = "company_api_key", required = false) String companyApiKeyParam,
            @RequestParam(value = "from") Long fromTimestamp,
            @RequestParam(value = "to") Long toTimestamp,
            @RequestParam(value = "sensor_id") List<Integer> sensorIds) {

    String companyApiKey = (companyApiKeyHeader != null) ? companyApiKeyHeader : companyApiKeyParam;
    if (companyApiKey == null || companyApiKey.isBlank()) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.emptyList());
    }

    List<SensorData> sensorDataList = sensorDataService.findFilteredData(companyApiKey, fromTimestamp, toTimestamp, sensorIds);
    List<SensorDataDTO> sensorDataDTOs = sensorDataList.stream()
                                                        .map(sensorDataMapper::toDTO)
                                                        .collect(Collectors.toList());
        return ResponseEntity.ok(sensorDataDTOs);
    }

}
