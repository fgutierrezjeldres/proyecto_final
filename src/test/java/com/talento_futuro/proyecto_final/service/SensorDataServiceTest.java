package com.talento_futuro.proyecto_final.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.talento_futuro.proyecto_final.dto.SensorDataDTO;
import com.talento_futuro.proyecto_final.entity.Company;
import com.talento_futuro.proyecto_final.entity.Sensor;
import com.talento_futuro.proyecto_final.entity.SensorData;
import com.talento_futuro.proyecto_final.exception.UnauthorizedException;
import com.talento_futuro.proyecto_final.mapper.SensorDataMapper;
import com.talento_futuro.proyecto_final.repository.ICompanyRepository;
import com.talento_futuro.proyecto_final.repository.IGenericRepository;
import com.talento_futuro.proyecto_final.repository.ISensorDataRepository;
import com.talento_futuro.proyecto_final.repository.ISensorRepository;
import com.talento_futuro.proyecto_final.service.impl.SensorDataServiceImpl;

@ExtendWith(MockitoExtension.class)
public class SensorDataServiceTest {

    @Mock
    private ISensorRepository sensorRepository;

    @Mock
    private ICompanyRepository companyRepository;

    @Mock
    private ISensorDataRepository sensorDataRepository;

    @Mock
    private SensorDataMapper sensorDataMapper;

    @Mock
    private IGenericRepository<SensorData, Integer> genericRepository;

    @InjectMocks
    private SensorDataServiceImpl service;

    @Test
    void shouldRegisterSensorDataSuccessfully() throws Exception {
        // Arrange
        String json = """
        {
            "api_key": "8c81d069-0883-4aa9-a6ce-7dc52c1e0a54",
            "json_data": [
                {
                    "datetime": 1742860430,
                    "temp": 24.4,
                    "humidity": 0.5
                },
                {
                    "datetime": 1742861495,
                    "temp": 22.1,
                    "humidity": 0.6
                }
            ]
        }""";

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(json);

        Sensor sensor = new Sensor();
        sensor.setId(1);
        sensor.setSensorApiKey("8c81d069-0883-4aa9-a6ce-7dc52c1e0a54");

        when(sensorRepository.findBySensorApiKey("8c81d069-0883-4aa9-a6ce-7dc52c1e0a54")).thenReturn(Optional.of(sensor));

        // Mapeamos cada dato
        when(sensorDataMapper.toEntity(any())).thenAnswer(inv -> {
            SensorDataDTO dto = inv.getArgument(0);
            SensorData entity = new SensorData();
            entity.setData(dto.getData());
            entity.setMetric(dto.getMetric());
            entity.setReceivedAt(dto.getReceivedAt());
            entity.setSensor(new Sensor());
            return entity;
        });

        when(genericRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        when(sensorDataMapper.toDTO(any())).thenAnswer(inv -> {
            SensorData sd = inv.getArgument(0);
            SensorDataDTO dto = new SensorDataDTO();
            dto.setData(sd.getData());
            dto.setMetric(sd.getMetric());
            dto.setReceivedAt(sd.getReceivedAt());
            return dto;
        });

        // Act
        List<SensorDataDTO> result = service.registerSensorData(jsonNode);

        // Assert
        assertEquals(4, result.size()); // 2 registros * 2 métricas (temp, humidity)
        assertTrue(result.stream().anyMatch(r -> r.getMetric().equals("temp")));
        assertTrue(result.stream().anyMatch(r -> r.getMetric().equals("humidity")));
    }

    @Test
    void shouldThrowUnauthorizedWhenSensorApiKeyIsInvalid() throws Exception {
        // Arrange
        String json = """
        {
            "api_key": "invalid-api-key",
            "json_data": []
        }""";

        JsonNode jsonNode = new ObjectMapper().readTree(json);
        when(sensorRepository.findBySensorApiKey("invalid-api-key")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UnauthorizedException.class, () -> service.registerSensorData(jsonNode));
    }

    @Test
    void shouldFindFilteredDataSuccessfully() {
        // Arrange
        String companyApiKey = "company-api-key";
        Long from = 1000L;
        Long to = 2000L;
        List<Integer> sensorIds = List.of(1, 2);

        Company company = new Company();
        when(companyRepository.findByCompanyApiKey(companyApiKey)).thenReturn(Optional.of(company));

        List<SensorData> expected = List.of(new SensorData(), new SensorData());
        when(sensorDataRepository.findSensorDataByFilters(company, from, to, sensorIds)).thenReturn(expected);

        // Act
        List<SensorData> result = service.findFilteredData(companyApiKey, from, to, sensorIds);

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void shouldThrowIfCompanyApiKeyIsMissing() {
        assertThrows(UnauthorizedException.class, () -> service.findFilteredData("", 1000L, 2000L, List.of(1)));
    }

    @Test
    void shouldThrowIfSensorIdsAreEmpty() {
        String apiKey = "key";
        when(companyRepository.findByCompanyApiKey(apiKey)).thenReturn(Optional.of(new Company()));
        assertThrows(IllegalArgumentException.class, () -> service.findFilteredData(apiKey, 1000L, 2000L, new ArrayList<>()));
    }
}
