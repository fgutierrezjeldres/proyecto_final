package com.talento_futuro.ProyectoFinal.dto.mapper;

import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.talento_futuro.ProyectoFinal.dto.SensorDTO;
import com.talento_futuro.ProyectoFinal.dto.SensorDataDTO;
import com.talento_futuro.ProyectoFinal.entity.Location;
import com.talento_futuro.ProyectoFinal.entity.Sensor;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SensorMapper {

    private final ModelMapper modelMapper;

    public SensorDTO toDTO(Sensor sensor) {
        SensorDTO sensorDTO = modelMapper.map(sensor, SensorDTO.class);
        sensorDTO.setSensorApiKey(sensor.getSensorApiKey());
        sensorDTO.setSensorData(sensor.getSensorData().stream()
                                      .map(sensorData -> modelMapper.map(sensorData, SensorDataDTO.class))
                                      .collect(Collectors.toList()));
        return sensorDTO;
    }

    public Sensor toEntity(SensorDTO sensorDTO, Location location) {
        Sensor sensor = modelMapper.map(sensorDTO, Sensor.class);
        sensor.setLocation(location);
        return sensor;
    }

}
