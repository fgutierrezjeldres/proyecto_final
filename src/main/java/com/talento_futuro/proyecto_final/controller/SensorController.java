package com.talento_futuro.proyecto_final.controller;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import com.talento_futuro.proyecto_final.dto.SensorDTO;
import com.talento_futuro.proyecto_final.mapper.SensorMapper;
import com.talento_futuro.proyecto_final.entity.Sensor;
import com.talento_futuro.proyecto_final.service.ISensorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/sensors")
@RequiredArgsConstructor
@Tag(name = "Sensors", description = "Endpoints para la gestión de sensores IoT")
public class SensorController {

        private final ISensorService sensorService;
        private final SensorMapper sensorMapper;

        @PostMapping
        @Operation(summary = "Registrar un sensor", description = "Crea un nuevo sensor en el sistema.")
        @ApiResponse(responseCode = "201", description = "Sensor creado exitosamente")
        public ResponseEntity<EntityModel<SensorDTO>> register(@Valid @RequestBody SensorDTO sensorDTO) {
            SensorDTO savedSensorDTO = sensorService.registerSensor(sensorDTO);
    
            EntityModel<SensorDTO> resource = EntityModel.of(savedSensorDTO,
                    linkTo(methodOn(SensorController.class).getSensorById(savedSensorDTO.getId())).withSelfRel(),
                    linkTo(methodOn(SensorController.class).updateSensor(savedSensorDTO.getId(), null)).withRel("update"),
                    linkTo(methodOn(SensorController.class).deleteSensor(savedSensorDTO.getId())).withRel("delete"));
    
            URI location = linkTo(methodOn(SensorController.class).getSensorById(savedSensorDTO.getId())).toUri();
            return ResponseEntity.created(location).body(resource);
        }
    
        @GetMapping("/{id}")
        @Operation(summary = "Obtener un sensor por ID", description = "Devuelve la información de un sensor específico.")
        @ApiResponse(responseCode = "200", description = "Sensor encontrado")
        @ApiResponse(responseCode = "404", description = "Sensor no encontrado")
        public ResponseEntity<EntityModel<SensorDTO>> getSensorById(@PathVariable Integer id) {
            Sensor sensor = sensorService.findById(id);
            SensorDTO sensorDTO = sensorMapper.toDTO(sensor);
    
            EntityModel<SensorDTO> resource = EntityModel.of(sensorDTO,
                    linkTo(methodOn(SensorController.class).getSensorById(id)).withSelfRel(),
                    linkTo(methodOn(SensorController.class).updateSensor(id, null)).withRel("update"),
                    linkTo(methodOn(SensorController.class).deleteSensor(id)).withRel("delete"));
    
            return ResponseEntity.ok(resource);
        }
    
        @PutMapping("/{id}")
        @Operation(summary = "Actualizar un sensor", description = "Modifica los datos de un sensor existente.")
        @ApiResponse(responseCode = "200", description = "Sensor actualizado exitosamente")
        public ResponseEntity<EntityModel<SensorDTO>> updateSensor(@PathVariable Integer id,
                                                                   @RequestBody SensorDTO sensorDTO) {
            SensorDTO updatedSensorDTO = sensorService.updateSensor(sensorDTO, id);
    
            EntityModel<SensorDTO> resource = EntityModel.of(updatedSensorDTO,
                    linkTo(methodOn(SensorController.class).getSensorById(id)).withSelfRel(),
                    linkTo(methodOn(SensorController.class).deleteSensor(id)).withRel("delete"));
    
            return ResponseEntity.ok(resource);
        }
    
        @DeleteMapping("/{id}")
        @Operation(summary = "Eliminar un sensor", description = "Elimina un sensor por su ID.")
        @ApiResponse(responseCode = "204", description = "Sensor eliminado exitosamente")
        public ResponseEntity<Void> deleteSensor(@PathVariable Integer id) {
            sensorService.delete(id);
            return ResponseEntity.noContent().build();
        }

        @GetMapping
        @Operation(summary = "Listar todos los sensores", description = "Obtiene una lista de todos los sensores registrados.")
        @ApiResponse(responseCode = "200", description = "Lista de sensores obtenida exitosamente")
        public ResponseEntity<CollectionModel<EntityModel<SensorDTO>>> getAllSensors() {
                List<Sensor> sensors = sensorService.findAll();
                List<SensorDTO> sensorDTOs = sensors.stream()
                                .map(sensorMapper::toDTO)
                                .collect(Collectors.toList());

                // Crear CollectionModel para los sensores
                List<EntityModel<SensorDTO>> resources = sensorDTOs.stream()
                                .map(sensorDTO -> {
                                        EntityModel<SensorDTO> resource = EntityModel.of(sensorDTO);
                                        resource.add(WebMvcLinkBuilder
                                                        .linkTo(WebMvcLinkBuilder.methodOn(SensorController.class)
                                                                        .getSensorById(sensorDTO.getId()))
                                                        .withSelfRel());
                                        return resource;
                                })
                                .collect(Collectors.toList());

                CollectionModel<EntityModel<SensorDTO>> collectionModel = CollectionModel.of(resources);
                collectionModel.add(WebMvcLinkBuilder.linkTo(SensorController.class).withSelfRel());

                return ResponseEntity.ok().body(collectionModel);
        }
}