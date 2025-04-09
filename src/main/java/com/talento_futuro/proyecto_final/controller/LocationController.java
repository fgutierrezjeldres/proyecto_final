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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/locations")
@RequiredArgsConstructor
@Tag(name = "Locations", description = "Endpoints para la gestión de ubicaciones")
public class LocationController {

        private final ILocationService locationService;
        private final LocationMapper locationMapper;

        @PostMapping
        @Operation(summary = "Registrar una ubicación", description = "Crea una nueva ubicación en el sistema.")
        @ApiResponse(responseCode = "201", description = "Ubicación creada exitosamente")
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
        @Operation(summary = "Obtener una ubicación por ID", description = "Devuelve la información de una ubicación específica.")
        @ApiResponse(responseCode = "200", description = "Ubicación encontrada")
        @ApiResponse(responseCode = "404", description = "Ubicación no encontrada")
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
        @Operation(summary = "Actualizar una ubicación", description = "Modifica los datos de una ubicación existente.")
        @ApiResponse(responseCode = "200", description = "Ubicación actualizada exitosamente")
        public ResponseEntity<LocationDTO> updateLocation(@PathVariable Integer id,
                        @RequestBody LocationDTO locationDTO) {
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
        @Operation(summary = "Eliminar una ubicación", description = "Elimina una ubicación por su ID.")
        @ApiResponse(responseCode = "204", description = "Ubicación eliminada exitosamente")
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

        @GetMapping
        @Operation(summary = "Listar todas las ubicaciones", description = "Obtiene una lista de todas las ubicaciones registradas.")
        @ApiResponse(responseCode = "200", description = "Lista de ubicaciones obtenida exitosamente")
        public ResponseEntity<CollectionModel<EntityModel<LocationDTO>>> getAllLocations() {
                List<Location> locations = locationService.findAll();
                List<EntityModel<LocationDTO>> locationResources = locations.stream()
                                .map(location -> {
                                        LocationDTO dto = locationMapper.toDTO(location);
                                        EntityModel<LocationDTO> model = EntityModel.of(dto);
                                        model.add(
                                                        WebMvcLinkBuilder.linkTo(
                                                                        WebMvcLinkBuilder.methodOn(
                                                                                        LocationController.class)
                                                                                        .getLocationById(dto.getId()))
                                                                        .withSelfRel());
                                        return model;
                                })
                                .collect(Collectors.toList());

                CollectionModel<EntityModel<LocationDTO>> collectionModel = CollectionModel.of(locationResources,
                                WebMvcLinkBuilder.linkTo(
                                                WebMvcLinkBuilder.methodOn(LocationController.class).getAllLocations())
                                                .withSelfRel());

                return ResponseEntity.ok(collectionModel);
        }
}