package com.talento_futuro.ProyectoFinal.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.talento_futuro.ProyectoFinal.dto.LocationDTO;
import com.talento_futuro.ProyectoFinal.dto.mapper.LocationMapper;
import com.talento_futuro.ProyectoFinal.entity.Company;
import com.talento_futuro.ProyectoFinal.entity.Location;
import com.talento_futuro.ProyectoFinal.repository.ICompanyRepository;
import com.talento_futuro.ProyectoFinal.repository.IGenericRepository;
import com.talento_futuro.ProyectoFinal.repository.ILocationRepository;
import com.talento_futuro.ProyectoFinal.service.ILocationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl extends CRUDServiceImpl<Location, Integer> implements ILocationService{

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
        Location location = locationMapper.toEntity(locationDTO, company);
        location.setId(existingLocation.getId());
        Location updatedLocation = repository.save(location);
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
