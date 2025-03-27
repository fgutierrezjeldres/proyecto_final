package com.talento_futuro.proyecto_final.controller;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.talento_futuro.proyecto_final.dto.SensorDTO;
import com.talento_futuro.proyecto_final.mapper.SensorMapper;
import com.talento_futuro.proyecto_final.entity.Sensor;
import com.talento_futuro.proyecto_final.service.ISensorService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/sensors")
@RequiredArgsConstructor
public class SensorController {

    private final ISensorService sensorService;
    private final SensorMapper sensorMapper;
    
    @PostMapping
    public ResponseEntity<SensorDTO> register(@RequestBody SensorDTO sensorDTO) {
        SensorDTO savedSensorDTO = sensorService.registerSensor(sensorDTO);

        URI location = ServletUriComponentsBuilder
                            .fromCurrentRequest()
                            .path("/{id}")
                            .queryParam("status", "created")
                            .buildAndExpand(savedSensorDTO.getId())    
                            .toUri();
        return ResponseEntity.created(location).body(savedSensorDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SensorDTO> getSensorById(@PathVariable Integer id) {
        Sensor sensor = sensorService.findById(id);
        SensorDTO sensorDTO = sensorMapper.toDTO(sensor);

        URI uri = ServletUriComponentsBuilder
                            .fromCurrentRequest()
                            .path("/{id}")
                            .queryParam("status", "fetched") 
                            .buildAndExpand(id)
                            .toUri();
        return ResponseEntity.ok()
                             .location(uri)
                             .body(sensorDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SensorDTO> updateSensor(@PathVariable Integer id, @RequestBody SensorDTO sensorDTO) {
        SensorDTO updatedSensorDTO = sensorService.updateSensor(sensorDTO, id);

        URI uri = ServletUriComponentsBuilder
                            .fromCurrentRequest()
                            .path("/{id}")
                            .queryParam("status", "updated") 
                            .buildAndExpand(id)
                            .toUri();

        return ResponseEntity.ok()
                             .location(uri)
                             .body(updatedSensorDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSensor(@PathVariable Integer id) {
        sensorService.delete(id);
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

    @GetMapping
    public ResponseEntity<List<SensorDTO>> getAllSensors() {
        List<Sensor> sensors = sensorService.findAll();
        List<SensorDTO> sensorDTOs = sensors.stream()
                                            .map(sensorMapper::toDTO)
                                            .collect(Collectors.toList());

        URI uri = ServletUriComponentsBuilder
                            .fromCurrentRequest()
                            .path("/all")
                            .queryParam("status", "fetched")
                            .build()
                            .toUri();

        return ResponseEntity.ok()
                             .location(uri)
                             .body(sensorDTOs);
    }
}
