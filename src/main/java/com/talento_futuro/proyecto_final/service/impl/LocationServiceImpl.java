package com.talento_futuro.proyecto_final.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.talento_futuro.proyecto_final.dto.LocationDTO;
import com.talento_futuro.proyecto_final.mapper.LocationMapper;
import com.talento_futuro.proyecto_final.entity.Company;
import com.talento_futuro.proyecto_final.entity.Location;
import com.talento_futuro.proyecto_final.repository.ICompanyRepository;
import com.talento_futuro.proyecto_final.repository.IGenericRepository;
import com.talento_futuro.proyecto_final.repository.ILocationRepository;
import com.talento_futuro.proyecto_final.service.ILocationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl extends CRUDServiceImpl<Location, Integer> implements ILocationService {

    private final ILocationRepository repository;
    private final LocationMapper locationMapper;
    private final ICompanyRepository companyRepository;

    @Override
    protected IGenericRepository<Location, Integer> getRepository() {
        return repository;
    }

    @Override
    @Transactional
    public LocationDTO updateLocation(LocationDTO locationDTO, Integer id) {

        Location existingLocation = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Location not found"));
        Company company = companyRepository.findById(locationDTO.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Company not found"));
        existingLocation.setLocationName(locationDTO.getLocationName());
        existingLocation.setLocationCountry(locationDTO.getLocationCountry());
        existingLocation.setLocationCity(locationDTO.getLocationCity());
        existingLocation.setLocationMeta(locationDTO.getLocationMeta());
        existingLocation.setCompany(company);
        Location updatedLocation = update(existingLocation, id);

        return locationMapper.toDTO(updatedLocation);
    }

    @Override
    @Transactional
    public LocationDTO registerLocation(LocationDTO locationDTO) {

        Company company = companyRepository.findById(locationDTO.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Company not found"));
        Location location = locationMapper.toEntity(locationDTO, company);
        Location savedLocation = repository.save(location);
        return locationMapper.toDTO(savedLocation);
    }

}
