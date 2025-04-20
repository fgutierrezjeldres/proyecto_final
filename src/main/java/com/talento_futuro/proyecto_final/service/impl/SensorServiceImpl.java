package com.talento_futuro.proyecto_final.service.impl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.talento_futuro.proyecto_final.dto.SensorDTO;
import com.talento_futuro.proyecto_final.mapper.SensorDataMapper;
import com.talento_futuro.proyecto_final.mapper.SensorMapper;
import com.talento_futuro.proyecto_final.entity.Company;
import com.talento_futuro.proyecto_final.entity.Location;
import com.talento_futuro.proyecto_final.entity.Sensor;
import com.talento_futuro.proyecto_final.entity.SensorData;
import com.talento_futuro.proyecto_final.repository.ICompanyRepository;
import com.talento_futuro.proyecto_final.repository.IGenericRepository;
import com.talento_futuro.proyecto_final.repository.ILocationRepository;
import com.talento_futuro.proyecto_final.repository.ISensorDataRepository;
import com.talento_futuro.proyecto_final.repository.ISensorRepository;
import com.talento_futuro.proyecto_final.service.ISensorService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SensorServiceImpl extends CRUDServiceImpl<Sensor, Integer> implements ISensorService {

    private final ISensorRepository repository;
    private final SensorMapper sensorMapper;
    private final SensorDataMapper sensorDataMapper;
    private final ILocationRepository locationRepository;
    private final ICompanyRepository companyRepository;
    private final ISensorDataRepository sensorDataRepository;

    @Override
    protected IGenericRepository<Sensor, Integer> getRepository() {
        return repository;
    }

    @Override
    public SensorDTO registerSensor(SensorDTO sensorDTO) {

        Company company = companyRepository.findByCompanyApiKey(sensorDTO.getCompanyApiKey())
                .orElseThrow(() -> new RuntimeException("Company not found"));

        Location location = company.getLocations().stream().filter(loc -> loc.getId().equals(sensorDTO.getLocationId()))
                .findFirst().orElseThrow(() -> new RuntimeException(
                        "The location with ID " + sensorDTO.getLocationId() + " does not belong to the company."));

        Sensor sensor = sensorMapper.toEntity(sensorDTO, location);
        if (sensor.getSensorApiKey() == null || sensor.getSensorApiKey().isEmpty()) {
            String apiKey = UUID.randomUUID().toString();
            sensor.setSensorApiKey(apiKey);
        }

        Sensor savedSensor = save(sensor);
        return sensorMapper.toDTO(savedSensor);
    }

    @Override
    @Transactional
    public SensorDTO updateSensor(SensorDTO sensorDTO, Integer id) {

        companyRepository.findByCompanyApiKey(sensorDTO.getCompanyApiKey())
                .orElseThrow(() -> new RuntimeException("Company not found"));

        Sensor existingSensor = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sensor not found"));

        Location location = locationRepository.findById(sensorDTO.getLocationId())
                .orElseThrow(() -> new RuntimeException("Location not found"));

        existingSensor.setSensorName(sensorDTO.getSensorName());
        existingSensor.setSensorCategory(sensorDTO.getSensorCategory());
        existingSensor.setSensorMeta(sensorDTO.getSensorMeta());
        existingSensor.setLastOnline(sensorDTO.getLastOnline());
        existingSensor.setStatus(sensorDTO.getStatus());
        existingSensor.setCommunicationProtocol(sensorDTO.getCommunicationProtocol());
        existingSensor.setLocation(location);
        Sensor savedSensor = repository.save(existingSensor);
        return sensorMapper.toDTO(savedSensor);
    }

    @Override
    public List<SensorDTO> getAllSensors(Integer  limit) {
    List<Sensor> sensors = repository.findAll();
    return sensors.stream()
        .map(sensor -> {
            SensorDTO dto = sensorMapper.toDTO(sensor);

            List<SensorData> sensorDataList;

            if (limit != null && limit > 0) {
                sensorDataList = sensorDataRepository.findBySensorIdOrderByReceivedAtDesc(
                    sensor.getId(), PageRequest.of(0, limit));
            } else {
                sensorDataList = sensorDataRepository.findBySensorIdOrderByReceivedAtDesc(sensor.getId());
            }
            dto.setSensorData(sensorDataList.stream()
                .map(sensorDataMapper::toDTO)
                .collect(Collectors.toList()));

            return dto;
        })
        .collect(Collectors.toList());
        
    }

}
