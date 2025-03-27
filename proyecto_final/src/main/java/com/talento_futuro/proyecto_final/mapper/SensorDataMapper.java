package com.talento_futuro.proyecto_final.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.talento_futuro.proyecto_final.dto.SensorDataDTO;
import com.talento_futuro.proyecto_final.entity.Sensor;
import com.talento_futuro.proyecto_final.entity.SensorData;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SensorDataMapper {

    private final ModelMapper modelMapper;

     public SensorDataDTO toDTO(SensorData sensorData) {
        return modelMapper.map(sensorData, SensorDataDTO.class);
    }

    public SensorData toEntity(SensorDataDTO sensorDataDTO) {
        SensorData sensorData = modelMapper.map(sensorDataDTO, SensorData.class);
        if (sensorDataDTO.getSensorId() != null) {
            Sensor sensor = new Sensor();
            sensor.setId(sensorDataDTO.getSensorId());
            sensorData.setSensor(sensor);
        }
        
        return sensorData;
        
    }

}
