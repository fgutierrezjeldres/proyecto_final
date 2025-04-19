package com.talento_futuro.proyecto_final.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.talento_futuro.proyecto_final.dto.SensorDataDTO;
import com.talento_futuro.proyecto_final.mapper.SensorDataMapper;
import com.talento_futuro.proyecto_final.entity.Company;
import com.talento_futuro.proyecto_final.entity.Sensor;
import com.talento_futuro.proyecto_final.entity.SensorData;
import com.talento_futuro.proyecto_final.exception.UnauthorizedException;
import com.talento_futuro.proyecto_final.repository.ICompanyRepository;
import com.talento_futuro.proyecto_final.repository.IGenericRepository;
import com.talento_futuro.proyecto_final.repository.ISensorDataRepository;
import com.talento_futuro.proyecto_final.repository.ISensorRepository;
import com.talento_futuro.proyecto_final.service.ISensorDataService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SensorDataServiceImpl extends CRUDServiceImpl<SensorData, Integer> implements ISensorDataService {

    private final IGenericRepository<SensorData, Integer> repository;
    private final SensorDataMapper sensorDataMapper;
    private final ISensorRepository sensorRepository;
    private final ICompanyRepository companyRepository;
    private final ISensorDataRepository sensorDataRepository;

    @Override
    protected IGenericRepository<SensorData, Integer> getRepository() {
        return repository;
    }

    @Override
    @Transactional
    public List<SensorDataDTO> registerSensorData(JsonNode jsonNode) {

        String apiKey = jsonNode.path("api_key").asText();
        /*if (!isValidSensorApiKey(apiKey)) {
            throw new UnauthorizedException("Invalid sensor API key");
        } */

        List<SensorDataDTO> sensorDataDTOList = processSensorData(jsonNode.get("json_data"), apiKey);
        return sensorDataDTOList.stream()
                .map(sensorDataMapper::toEntity)
                .map(this::save)
                .map(sensorDataMapper::toDTO)
                .collect(Collectors.toList());
    }

    /*@Override
    public boolean isValidSensorApiKey(String sensorApiKey) {
        
        return sensorRepository.findBySensorApiKey(sensorApiKey)
                .map(sensor -> sensorApiKey.equals(sensor.getSensorApiKey()))
                .orElse(false);

    }*/

    private List<SensorDataDTO> processSensorData(JsonNode jsonData, String apiKey) {
        List<SensorDataDTO> sensorDataList = new ArrayList<>();

        Sensor sensor = sensorRepository.findBySensorApiKey(apiKey)
                .orElseThrow(() -> new UnauthorizedException(
                        "Invalid sensor API key: " + apiKey));

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
                    sensorDataList.add(sensorDataDTO);
                }
            });
        }

        return sensorDataList;
    }

    @Override
    public List<SensorData> findFilteredData(String companyApiKey, Long from, Long to, List<Integer> sensorIds) {

        if (companyApiKey == null || companyApiKey.isBlank()) {
            throw new UnauthorizedException("company_api_key is required");
        }
        Company company = companyRepository.findByCompanyApiKey(companyApiKey)
                .orElseThrow(() -> new UnauthorizedException("api_key not found: " + companyApiKey));
        if (from == null || to == null) {
            throw new IllegalArgumentException("Both 'from' and 'to' epoch are required");
        }
        if (sensorIds == null || sensorIds.isEmpty()) {
            throw new IllegalArgumentException("At least one sensor_id must be provided");
        }
        return sensorDataRepository.findSensorDataByFilters(company, from, to, sensorIds);
    }

    @Override
    @Transactional
    public SensorDataDTO updateSensorData(SensorDataDTO sensorDataDTO, Integer id) {

        SensorData existingSensorData = sensorDataRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SensorData not found"));

        Sensor sensor = sensorRepository.findById(sensorDataDTO.getSensorId())
                .orElseThrow(() -> new RuntimeException("Sensor not found"));

        existingSensorData.setData(sensorDataDTO.getData());
        existingSensorData.setMetric(sensorDataDTO.getMetric());
        existingSensorData.setReceivedAt(sensorDataDTO.getReceivedAt());
        existingSensorData.setSensor(sensor);

        SensorData updatedSensorData = sensorDataRepository.save(existingSensorData);
        return sensorDataMapper.toDTO(updatedSensorData);
    }

}
