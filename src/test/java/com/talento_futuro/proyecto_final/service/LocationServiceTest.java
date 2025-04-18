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


import com.talento_futuro.proyecto_final.dto.LocationDTO;
import com.talento_futuro.proyecto_final.entity.Company;
import com.talento_futuro.proyecto_final.entity.Location;
import com.talento_futuro.proyecto_final.mapper.LocationMapper;
import com.talento_futuro.proyecto_final.repository.ICompanyRepository;
import com.talento_futuro.proyecto_final.repository.ILocationRepository;
import com.talento_futuro.proyecto_final.service.impl.LocationServiceImpl;

@ExtendWith(MockitoExtension.class)
public class LocationServiceTest {

    @Mock
    private ILocationRepository locationRepository;

    @Mock
    private ICompanyRepository companyRepository;

    @Mock
    private LocationMapper locationMapper;

    @InjectMocks
    private LocationServiceImpl locationService;

    @Test
    void shouldRegisterLocationSuccessfully() {
        // Arrange
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setCompanyId(1);
        locationDTO.setLocationName("Test Location");
        locationDTO.setLocationCountry("Test Country");
        locationDTO.setLocationCity("Test City");
        locationDTO.setLocationMeta("Some metadata");

        // Crear una compañía mockeada
        Company company = new Company();
        company.setId(1);
        company.setCompanyName("Company A");
        company.setCompanyApiKey("existing-api-key");

        // Crear la ubicación mockeada
        Location location = new Location();
        location.setId(10);
        location.setLocationName("Test Location");
        location.setLocationCountry("Test Country");
        location.setLocationCity("Test City");
        location.setLocationMeta("Some metadata");
        location.setCompany(company);

        // Mocks
        when(companyRepository.findById(1)).thenReturn(Optional.of(company));
        when(locationMapper.toEntity(locationDTO, company)).thenReturn(location);
        when(locationRepository.save(any(Location.class))).thenReturn(location);
        when(locationMapper.toDTO(location)).thenReturn(locationDTO);

        // Act
        LocationDTO result = locationService.registerLocation(locationDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Test Location", result.getLocationName());
        assertEquals("Test Country", result.getLocationCountry());
        assertEquals("Test City", result.getLocationCity());
        assertEquals("Some metadata", result.getLocationMeta());
        assertEquals(1, result.getCompanyId());
    }

    @Test
    void shouldUpdateLocationSuccessfully() {
        // Arrange
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setCompanyId(1);
        locationDTO.setLocationName("Updated Location");
        locationDTO.setLocationCountry("Updated Country");
        locationDTO.setLocationCity("Updated City");
        locationDTO.setLocationMeta("Updated metadata");

        // Crear una compañía mockeada
        Company company = new Company();
        company.setId(1);
        company.setCompanyName("Company A");
        company.setCompanyApiKey("79f3bf42-5e1f-4855-a8a3-734fdee9f884");

        // Crear la ubicación existente mockeada
        Location existingLocation = new Location();
        existingLocation.setId(1);
        existingLocation.setLocationName("Old Location");
        existingLocation.setLocationCountry("Old Country");
        existingLocation.setLocationCity("Old City");
        existingLocation.setLocationMeta("Old metadata");
        existingLocation.setCompany(company);

        // Crear la ubicación actualizada mockeada
        Location updatedLocation = new Location();
        updatedLocation.setId(1);
        updatedLocation.setLocationName("Updated Location");
        updatedLocation.setLocationCountry("Updated Country");
        updatedLocation.setLocationCity("Updated City");
        updatedLocation.setLocationMeta("Updated metadata");
        updatedLocation.setCompany(company);

        // Mocks
        when(locationRepository.findById(1)).thenReturn(Optional.of(existingLocation));
        when(companyRepository.findById(1)).thenReturn(Optional.of(company));
        when(locationMapper.toEntity(locationDTO, company)).thenReturn(updatedLocation);
        when(locationRepository.save(any(Location.class))).thenReturn(updatedLocation);

        // Act
        LocationDTO result = locationService.updateLocation(locationDTO, 1);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Location", result.getLocationName());
        assertEquals("Updated Country", result.getLocationCountry());
        assertEquals("Updated City", result.getLocationCity());
        assertEquals("Updated metadata", result.getLocationMeta());
        assertEquals(1, result.getCompanyId());
    }

    @Test
    void shouldThrowExceptionIfLocationNotFoundDuringUpdate() {
        // Arrange
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setCompanyId(1);
        locationDTO.setLocationName("Updated Location");

        // Mocks
        when(locationRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> locationService.updateLocation(locationDTO, 1));
        assertEquals("Location not found", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionIfCompanyNotFoundDuringRegister() {
        // Arrange
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setCompanyId(1);
        locationDTO.setLocationName("Test Location");

        // Mocks
        when(companyRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> locationService.registerLocation(locationDTO));
        assertEquals("Company not found", exception.getMessage());
    }
}