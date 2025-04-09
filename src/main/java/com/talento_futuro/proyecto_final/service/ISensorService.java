package com.talento_futuro.proyecto_final.service;


import com.talento_futuro.proyecto_final.dto.SensorDTO;
import com.talento_futuro.proyecto_final.entity.Sensor;

public interface ISensorService extends ICRUDService<Sensor, Integer>{

     SensorDTO registerSensor(SensorDTO sensorDTO);
     SensorDTO updateSensor(SensorDTO sensorDTO, Integer id);
     
}
