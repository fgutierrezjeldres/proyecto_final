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

import com.talento_futuro.proyecto_final.dto.LocationDTO;
import com.talento_futuro.proyecto_final.mapper.LocationMapper;
import com.talento_futuro.proyecto_final.entity.Location;
import com.talento_futuro.proyecto_final.service.ILocationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

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
        public ResponseEntity<EntityModel<LocationDTO>> register(@Valid @RequestBody LocationDTO locationDTO) {
                LocationDTO savedLocationDTO = locationService.registerLocation(locationDTO);

                EntityModel<LocationDTO> resource = EntityModel.of(savedLocationDTO,
                                linkTo(methodOn(LocationController.class).getLocationById(savedLocationDTO.getId()))
                                                .withSelfRel(),
                                linkTo(methodOn(LocationController.class).updateLocation(savedLocationDTO.getId(),
                                                null)).withRel("update"),
                                linkTo(methodOn(LocationController.class).deleteLocation(savedLocationDTO.getId()))
                                                .withRel("delete"));

                URI location = linkTo(methodOn(LocationController.class).getLocationById(savedLocationDTO.getId()))
                                .toUri();
                return ResponseEntity.created(location).body(resource);
        }

        @GetMapping("/{id}")
        @Operation(summary = "Obtener una ubicación por ID", description = "Devuelve la información de una ubicación específica.")
        @ApiResponse(responseCode = "200", description = "Ubicación encontrada")
        @ApiResponse(responseCode = "404", description = "Ubicación no encontrada")
        public ResponseEntity<EntityModel<LocationDTO>> getLocationById(@PathVariable Integer id) {
                Location location = locationService.findById(id);
                LocationDTO locationDTO = locationMapper.toDTO(location);

                EntityModel<LocationDTO> resource = EntityModel.of(locationDTO,
                                linkTo(methodOn(LocationController.class).getLocationById(id)).withSelfRel(),
                                linkTo(methodOn(LocationController.class).updateLocation(id, null)).withRel("update"),
                                linkTo(methodOn(LocationController.class).deleteLocation(id)).withRel("delete"));

                return ResponseEntity.ok(resource);
        }

        @PutMapping("/{id}")
        @Operation(summary = "Actualizar una ubicación", description = "Modifica los datos de una ubicación existente.")
        @ApiResponse(responseCode = "200", description = "Ubicación actualizada exitosamente")
        public ResponseEntity<EntityModel<LocationDTO>> updateLocation(@PathVariable Integer id,
                        @RequestBody LocationDTO locationDTO) {
                LocationDTO updatedLocationDTO = locationService.updateLocation(locationDTO, id);

                EntityModel<LocationDTO> resource = EntityModel.of(updatedLocationDTO,
                                linkTo(methodOn(LocationController.class).getLocationById(id)).withSelfRel(),
                                linkTo(methodOn(LocationController.class).deleteLocation(id)).withRel("delete"));

                return ResponseEntity.ok(resource);
        }

        @DeleteMapping("/{id}")
        @Operation(summary = "Eliminar una ubicación", description = "Elimina una ubicación por su ID.")
        @ApiResponse(responseCode = "204", description = "Ubicación eliminada exitosamente")
        public ResponseEntity<Void> deleteLocation(@PathVariable Integer id) {
                locationService.delete(id);
                return ResponseEntity.noContent().build();
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
                                                        linkTo(
                                                                        methodOn(
                                                                                        LocationController.class)
                                                                                        .getLocationById(dto.getId()))
                                                                        .withSelfRel());
                                        return model;
                                })
                                .collect(Collectors.toList());

                CollectionModel<EntityModel<LocationDTO>> collectionModel = CollectionModel.of(locationResources,
                                linkTo(
                                                methodOn(LocationController.class).getAllLocations())
                                                .withSelfRel());

                return ResponseEntity.ok(collectionModel);
        }
}