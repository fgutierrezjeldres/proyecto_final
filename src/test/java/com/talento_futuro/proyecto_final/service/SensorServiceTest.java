package com.talento_futuro.proyecto_final.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.talento_futuro.proyecto_final.dto.SensorDTO;
import com.talento_futuro.proyecto_final.entity.Location;
import com.talento_futuro.proyecto_final.entity.Sensor;
import com.talento_futuro.proyecto_final.mapper.SensorMapper;
import com.talento_futuro.proyecto_final.repository.ILocationRepository;
import com.talento_futuro.proyecto_final.repository.ISensorRepository;
import com.talento_futuro.proyecto_final.service.impl.SensorServiceImpl;

@ExtendWith(MockitoExtension.class)
public class SensorServiceTest {

    @Mock
    private ISensorRepository sensorRepository;

    @Mock
    private ILocationRepository locationRepository;

    @Mock
    private SensorMapper sensorMapper;

    @InjectMocks
    private SensorServiceImpl sensorService;

    @Test
    void shouldRegisterSensorSuccessfully() {
        // Arrange
        SensorDTO sensorDTO = new SensorDTO();
        sensorDTO.setLocationId(1);
        sensorDTO.setSensorName("Sensor Temp");

        Location location = new Location();
        location.setId(1);
        location.setLocationName("Loc Norte");

        Sensor sensorEntity = new Sensor();
        sensorEntity.setSensorName("Sensor Temp");
        sensorEntity.setLocation(location);

        Sensor savedSensor = new Sensor();
        savedSensor.setId(1);
        savedSensor.setSensorName("Sensor Temp");
        savedSensor.setSensorApiKey("random-key");
        savedSensor.setLocation(location);

        SensorDTO expectedDTO = new SensorDTO();
        expectedDTO.setId(1);
        expectedDTO.setSensorName("Sensor Temp");
        expectedDTO.setSensorApiKey("random-key");
        expectedDTO.setLocationId(1);

        when(locationRepository.findById(1)).thenReturn(Optional.of(location));
        when(sensorMapper.toEntity(sensorDTO, location)).thenReturn(sensorEntity);
        when(sensorRepository.save(any(Sensor.class))).thenAnswer(invocation -> {
            Sensor sensor = invocation.getArgument(0);
            sensor.setId(1);
            sensor.setSensorApiKey("random-key");
            return sensor;
        });
        when(sensorMapper.toDTO(any(Sensor.class))).thenReturn(expectedDTO);

        // Act
        SensorDTO result = sensorService.registerSensor(sensorDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Sensor Temp", result.getSensorName());
        assertEquals("random-key", result.getSensorApiKey());
        assertEquals(1, result.getLocationId());
    }

    @Test
    void shouldUpdateSensorSuccessfully() {
        // Arrange
        SensorDTO sensorDTO = new SensorDTO();
        sensorDTO.setLocationId(2);
        sensorDTO.setSensorName("Updated Sensor");

        Location location = new Location();
        location.setId(2);
        location.setLocationName("Loc Sur");

        Sensor existingSensor = new Sensor();
        existingSensor.setId(5);
        existingSensor.setSensorApiKey("existing-api-key");

        Sensor updatedSensor = new Sensor();
        updatedSensor.setId(5);
        updatedSensor.setSensorApiKey("existing-api-key");
        updatedSensor.setSensorName("Updated Sensor");
        updatedSensor.setLocation(location);

        SensorDTO expectedDTO = new SensorDTO();
        expectedDTO.setId(5);
        expectedDTO.setSensorName("Updated Sensor");
        expectedDTO.setSensorApiKey("existing-api-key");
        expectedDTO.setLocationId(2);

        when(sensorRepository.findById(5)).thenReturn(Optional.of(existingSensor));
        when(locationRepository.findById(2)).thenReturn(Optional.of(location));
        when(sensorMapper.toEntity(sensorDTO, location)).thenReturn(updatedSensor);
        when(sensorRepository.save(any(Sensor.class))).thenReturn(updatedSensor);
        when(sensorMapper.toDTO(updatedSensor)).thenReturn(expectedDTO);

        // Act
        SensorDTO result = sensorService.updateSensor(sensorDTO, 5);

        // Assert
        assertNotNull(result);
        assertEquals(5, result.getId());
        assertEquals("Updated Sensor", result.getSensorName());
        assertEquals("existing-api-key", result.getSensorApiKey());
    }

    @Test
    void shouldThrowExceptionWhenLocationNotFoundOnRegister() {
        // Arrange
        SensorDTO sensorDTO = new SensorDTO();
        sensorDTO.setLocationId(999);

        when(locationRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> sensorService.registerSensor(sensorDTO));
        assertEquals("Location not found", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenSensorNotFoundOnUpdate() {
        // Arrange
        SensorDTO sensorDTO = new SensorDTO();
        sensorDTO.setLocationId(1);

        when(sensorRepository.findById(10)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> sensorService.updateSensor(sensorDTO, 10));
        assertEquals("Sensor not found", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenLocationNotFoundOnUpdate() {
        // Arrange
        SensorDTO sensorDTO = new SensorDTO();
        sensorDTO.setLocationId(123);

        Sensor existingSensor = new Sensor();
        existingSensor.setId(20);
        existingSensor.setSensorApiKey("key");

        when(sensorRepository.findById(20)).thenReturn(Optional.of(existingSensor));
        when(locationRepository.findById(123)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> sensorService.updateSensor(sensorDTO, 20));
        assertEquals("Location not found", exception.getMessage());
    }
}
