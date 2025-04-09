package com.talento_futuro.proyecto_final.controller;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.talento_futuro.proyecto_final.dto.LocationDTO;
import com.talento_futuro.proyecto_final.mapper.LocationMapper;
import com.talento_futuro.proyecto_final.entity.Location;
import com.talento_futuro.proyecto_final.service.ILocationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/locations")
@RequiredArgsConstructor
public class LocationController {

    private final ILocationService locationService;
    private final LocationMapper locationMapper;
    
    @PostMapping("/register")
    public ResponseEntity<LocationDTO> register(@RequestBody LocationDTO locationDTO) {
        LocationDTO savedLocationDTO = locationService.registerLocation(locationDTO);

        URI location = ServletUriComponentsBuilder
                            .fromCurrentRequest()
                            .path("/{id}")
                            .queryParam("status", "created")
                            .buildAndExpand(savedLocationDTO.getId())    
                            .toUri();
        return ResponseEntity.created(location).body(savedLocationDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocationDTO> getLocationById(@PathVariable Integer id) {
        Location location = locationService.findById(id);
        LocationDTO locationDTO = locationMapper.toDTO(location);

        URI uri = ServletUriComponentsBuilder
                            .fromCurrentRequest()
                            .path("/{id}")
                            .queryParam("status", "fetched")
                            .buildAndExpand(id)
                            .toUri();
        return ResponseEntity.ok()
                             .location(uri)
                             .body(locationDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LocationDTO> updateLocation(@PathVariable Integer id, @RequestBody LocationDTO locationDTO) {
        LocationDTO updatedLocationDTO = locationService.updateLocation(locationDTO, id);
        URI location = ServletUriComponentsBuilder
                            .fromCurrentRequest()
                            .path("/{id}")
                            .queryParam("status", "updated")
                            .buildAndExpand(id)
                            .toUri();

        return ResponseEntity.ok().location(location).body(updatedLocationDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Integer id) {
        locationService.delete(id);
        URI uri = ServletUriComponentsBuilder
                            .fromCurrentRequest()
                            .path("/{id}")
                            .queryParam("status", "deleted")
                            .buildAndExpand(id)
                            .toUri();
        return ResponseEntity.noContent()
                             .location(uri)
                             .build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<LocationDTO>> getAllLocations() {
        List<Location> locations = locationService.findAll();
        List<LocationDTO> locationDTOs = locations.stream()
                                                  .map(locationMapper::toDTO)
                                                  .collect(Collectors.toList());

        URI uri = ServletUriComponentsBuilder
                            .fromCurrentRequest()
                            .path("/all")
                            .queryParam("status", "fetched")
                            .build()
                            .toUri();

        return ResponseEntity.ok()
                             .location(uri)
                             .body(locationDTOs);
    }
}
