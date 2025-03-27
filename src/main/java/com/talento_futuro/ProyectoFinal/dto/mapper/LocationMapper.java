package com.talento_futuro.ProyectoFinal.dto.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.talento_futuro.ProyectoFinal.dto.LocationDTO;
import com.talento_futuro.ProyectoFinal.entity.Company;
import com.talento_futuro.ProyectoFinal.entity.Location;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LocationMapper {

    private final ModelMapper modelMapper;

     public LocationDTO toDTO(Location location) {
        return modelMapper.map(location, LocationDTO.class);
    }

    public Location toEntity(LocationDTO locationDTO, Company company) {
        Location location = modelMapper.map(locationDTO, Location.class);
        location.setCompany(company); 
        return location;
    }

}
