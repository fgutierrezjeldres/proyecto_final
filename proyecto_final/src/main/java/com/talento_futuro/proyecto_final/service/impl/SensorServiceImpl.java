package com.talento_futuro.proyecto_final.service.impl;

import java.util.UUID;

//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.talento_futuro.proyecto_final.dto.SensorDTO;
import com.talento_futuro.proyecto_final.mapper.SensorMapper;
import com.talento_futuro.proyecto_final.entity.Location;
import com.talento_futuro.proyecto_final.entity.Sensor;
import com.talento_futuro.proyecto_final.repository.IGenericRepository;
import com.talento_futuro.proyecto_final.repository.ILocationRepository;
import com.talento_futuro.proyecto_final.repository.ISensorRepository;
import com.talento_futuro.proyecto_final.service.ISensorService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SensorServiceImpl extends CRUDServiceImpl<Sensor, Integer> implements ISensorService{

    private final ISensorRepository repository;
    private final SensorMapper sensorMapper;
  //  private final PasswordEncoder encoder;
    private final ILocationRepository locationRepository;

    @Override
    protected IGenericRepository<Sensor, Integer> getRepository() {
        return repository;
    }
    
    @Override
    public SensorDTO registerSensor(SensorDTO sensorDTO) {

        Location location = locationRepository.findById(sensorDTO.getLocationId())
                .orElseThrow(() -> new RuntimeException("Location not found"));
        Sensor sensor = sensorMapper.toEntity(sensorDTO, location);
        if (sensor.getSensorApiKey() == null || sensor.getSensorApiKey().isEmpty()) {
            String apiKey = UUID.randomUUID().toString(); 
            //String apiKeyEncrypted = encoder.encode(apiKey); 
            sensor.setSensorApiKey(apiKey);  
        }

        Sensor savedSensor = save(sensor);
        return sensorMapper.toDTO(savedSensor);
    }
    @Override
    @Transactional
    public SensorDTO updateSensor(SensorDTO sensorDTO, Integer id) {

        Sensor existingSensor = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sensor not found"));

        Location location = locationRepository.findById(sensorDTO.getLocationId())
                .orElseThrow(() -> new RuntimeException("Location not found"));

        Sensor updatedSensor = sensorMapper.toEntity(sensorDTO, location);
        updatedSensor.setId(existingSensor.getId());
        updatedSensor.setSensorApiKey(existingSensor.getSensorApiKey());
        Sensor savedSensor = repository.save(updatedSensor);
        return sensorMapper.toDTO(savedSensor);
    }

}
