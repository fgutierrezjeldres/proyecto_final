package com.talento_futuro.ProyectoFinal.service;


import com.talento_futuro.ProyectoFinal.dto.SensorDTO;
import com.talento_futuro.ProyectoFinal.entity.Sensor;

public interface ISensorService extends ICRUDService<Sensor, Integer>{

     SensorDTO registerSensor(SensorDTO sensorDTO);
     SensorDTO updateSensor(SensorDTO sensorDTO, Integer id);
     
}
